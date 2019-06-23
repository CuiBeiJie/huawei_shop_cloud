package com.huawei.order.service.impl;
import com.huawei.auth.entity.UserInfo;
import com.huawei.common.entity.CartDto;
import com.huawei.common.enums.ExceptionEnums;
import com.huawei.common.exception.SelfException;
import com.huawei.common.utils.IdWorker;
import com.huawei.common.utils.JsonUtils;
import com.huawei.item.pojo.Sku;
import com.huawei.order.client.GoodsClient;
import com.huawei.order.client.ShippingClient;
import com.huawei.order.dto.OrderDto;
import com.huawei.order.enums.OrderStatusEnum;
import com.huawei.order.interceptor.UserInterCeptor;
import com.huawei.order.mapper.OrderDetailMapper;
import com.huawei.order.mapper.OrderMapper;
import com.huawei.order.mapper.OrderStatusMapper;
import com.huawei.order.pojo.Order;
import com.huawei.order.pojo.OrderDetail;
import com.huawei.order.pojo.OrderStatus;
import com.huawei.common.entity.Shipping;
import com.huawei.order.service.OrderService;
import com.huawei.order.utils.PayHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: cuibeijie
 * @Date: 2019/6/21 21:42
 * @Description: 订单接口实现层
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;//订单
    @Autowired
    private OrderDetailMapper orderDetailMapper;//订单详情
    @Autowired
    private OrderStatusMapper orderStatusMapper;//订单状态
    @Autowired
    private IdWorker idWorker; //id生成工具类
    @Autowired
    private ShippingClient shippingClient;//物流
    @Autowired
    private GoodsClient goodsClient;//商品
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private PayHelper payHelper;

    /**
     * 创建订单
     * @param orderDto
     * @return
     */
    @Transactional
    public Long createOrder(OrderDto orderDto) {
        // 生成orderId
        long orderId = idWorker.nextId();
        //获取登录用户信息
        UserInfo loginUser = UserInterCeptor.getLoginUser();
        //1.新增订单
        Order order = new Order();
        order.setOrderId(orderId);//订单id
        order.setPaymentType(orderDto.getPaymentType());//支付方式
        order.setBuyerNick(loginUser.getName());//买家昵称
        order.setBuyerRate(false); //是否已经评价
        order.setCreateTime(new Date());//创建时间
        order.setUserId(loginUser.getId());//用户id
        //收货人信息
        Shipping shipping = shippingClient.queryShippingById(orderDto.getAddressId().intValue());
        order.setReceiver(shipping.getName());//收货人全名
        order.setReceiverAddress(shipping.getAddress());
        order.setReceiverCity(shipping.getCity());
        order.setReceiverDistrict(shipping.getDistrict());
        order.setReceiverMobile(shipping.getPhone());
        order.setReceiverState(shipping.getState());
        order.setReceiverZip(shipping.getZipcode());
        //金额相关信息组装
        //把CartDto转成一个map，key是skuId，value是num
        Map<Long, Integer> cartMap = orderDto.getCarts().stream().
                collect(Collectors.toMap(CartDto::getSkuId, CartDto::getNum));
        //获取所有的skuIds
        Set<Long> skuIds = cartMap.keySet();
        //根据skuIds批量查询sku
        List<Sku> skuList = goodsClient.querySkusByIds(new ArrayList<>(skuIds));
        Long totalPay = 0L;
        //准备订单详情集合
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (Sku sku : skuList) {
            //计算总金额
            totalPay += sku.getPrice() * cartMap.get(sku.getId());
            //填充订单详情
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setImage(StringUtils.substringBefore(sku.getImages(),","));
            orderDetail.setNum(cartMap.get(sku.getId()));
            orderDetail.setOrderId(orderId);
            orderDetail.setOwnSpec(sku.getOwnSpec());
            orderDetail.setPrice(sku.getPrice());
            orderDetail.setSkuId(sku.getId());
            orderDetail.setTitle(sku.getTitle());
            orderDetails.add(orderDetail);
        }
        //总金额
        order.setTotalPay(totalPay);
        //实付金额 = 总金额 + 邮费 - 优惠金额
        order.setActualPay(totalPay + order.getPostFee() - 0);
        //把order写入数据库
        int count = orderMapper.insert(order);
        if(count != 1){
            log.error("[创建订单 ] 创建订单失败, orderId:{} ", orderId);
            throw new SelfException(ExceptionEnums.CREATE_ORDER_FAIL);
        }

        // 2. 新增订单详情
        count = orderDetailMapper.insertList(orderDetails);
        if(count != orderDetails.size()){
            log.error("[创建订单 ] 创建订单详情失败, orderId:{} ", orderId);
            throw new SelfException(ExceptionEnums.CREATE_ORDERDETAIL_FAIL);
        }

        //3.新增订单状态
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setCreateTime(order.getCreateTime());
        orderStatus.setOrderId(orderId);
        orderStatus.setStatus(OrderStatusEnum.UN_PAY.value());
        orderStatusMapper.insertSelective(orderStatus);

        //4.减库存
        List<CartDto> cartDtos = orderDto.getCarts();
        goodsClient.decreaseStock(cartDtos);

        //todo 删除购物车中已经下单的商品数据, 采用异步mq的方式通知购物车系统删除已购买的商品，传送商品ID和用户ID
        HashMap<String, Object> map = new HashMap<>();
        try {
            map.put("skuIds", cartMap.keySet());
            map.put("userId", loginUser.getId());
            amqpTemplate.convertAndSend("huawei.cart.exchange", "cart.delete", JsonUtils.serialize(map));
        } catch (Exception e) {
            log.error("删除购物车消息发送异常，商品ID：{}", cartMap.keySet(), e);
        }

        log.info("生成订单，订单编号：{}，用户id：{}", orderId, loginUser.getId());
        //返回订单id
        return orderId;
    }

    /**
     * 根据订单id查询订单详情
     * @param orderId
     * @return
     */
    public Order queryById(Long orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order == null) {
            throw new SelfException(ExceptionEnums.ORDER_NOT_FOUND);
        }
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orderId);
        List<OrderDetail> orderDetails = orderDetailMapper.select(orderDetail);
        if(CollectionUtils.isEmpty(orderDetails)){
            throw new SelfException(ExceptionEnums.ORDER_NOT_FOUND);
        }
        order.setOrderDetails(orderDetails);
        OrderStatus orderStatus = orderStatusMapper.selectByPrimaryKey(orderId);
        if(orderStatus == null){
            throw new SelfException(ExceptionEnums.ORDER_NOT_FOUND);
        }
        order.setOrderStatus(orderStatus);
        return order;
    }

    /**
     * 生成微信支付链接
     * @param orderId
     * @return
     */
    public String createPayUrl(Long orderId) {
        //先查询订单
        Order order = queryById(orderId);
        Integer status = order.getOrderStatus().getStatus();
        //判断订单订单是否为未支付状态
        if(OrderStatusEnum.UN_PAY.value() != status){
            throw new SelfException(ExceptionEnums.ORDER_STATUS_EXCEPTION);
        }
        String desc = order.getOrderDetails().get(0).getTitle();
        Long actualPay = /*order.getActualPay()*/ 1L;//实际为order.getActualPay(),测试的时候改为1分钱
        return payHelper.createPayUrl(orderId,desc,actualPay);
    }

    /**
     * 校验微信回调结果
     * @param msg
     */
    public void handleNotify(Map<String, String> msg) {
        //校验通信和业务标识
        payHelper.isSuccess(msg);
        //校验签名
        payHelper.isSignatureValid(msg);
        //校验金额
        String totalFee = msg.get("total_fee");  //订单金额
        String outTradeNo = msg.get("out_trade_no");  //订单编号
        if (StringUtils.isBlank(totalFee) || StringUtils.isBlank(outTradeNo)) {
            log.error("【微信支付回调】支付回调返回数据不正确");
            throw new SelfException(ExceptionEnums.WX_PAY_NOTIFY_PARAM_ERROR);
        }
        //查询订单
        Order order = orderMapper.selectByPrimaryKey(Long.valueOf(outTradeNo));

        //todo 这里验证回调数据时，支付金额使用1分进行验证，后续使用实际支付金额验证
        if (/*order.getActualPay()*/1 != Long.valueOf(totalFee)) {
            log.error("【微信支付回调】支付回调返回数据不正确");
            throw new SelfException(ExceptionEnums.WX_PAY_NOTIFY_PARAM_ERROR);
        }

        //判断支付状态
        OrderStatus orderStatus = orderStatusMapper.selectByPrimaryKey(Long.valueOf(outTradeNo));

        if (orderStatus.getStatus() != OrderStatusEnum.UN_PAY.value()) {
            //支付成功
            return;
        }

        //修改订单状态
        OrderStatus status = new OrderStatus();
        status.setStatus(OrderStatusEnum.PAY_UP.value());
        status.setOrderId(order.getOrderId());
        status.setPaymentTime(new Date());
        int count = orderStatusMapper.updateByPrimaryKeySelective(status);
        if(count !=1 ){
            throw new SelfException(ExceptionEnums.UPDATE_ORDERSTATUS_FAIL);
        }
        log.info("[订单回调] 订单支付成功！ 订单编号：{}",outTradeNo);
    }
}
