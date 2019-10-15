package mall.payment.controller;

import bean.Order;
import bean.PaymentInfo;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import mall.annotations.LoginRequired;
import mall.payment.config.AlipayConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import service.OrderService;
import service.PaymentService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PaymentController {
    @Autowired
    AlipayClient alipayClient;
    @Reference
    OrderService orderService;
    @Autowired
    PaymentService paymentService;
    @RequestMapping("index")
    @LoginRequired(loginSuccess = true)
    public String index(String outTradeNo, BigDecimal totalAmount,HttpServletRequest request, ModelMap modelMap){
        String memberId= (String) request.getAttribute("memberId");
        String nickname= (String) request.getAttribute("nickname");
        modelMap.put("nickname",nickname);
        modelMap.put("outTradeNo",outTradeNo);
        modelMap.put("totalAmount",totalAmount);
        return "index";
    }
    @RequestMapping("alipay/submit")
    @LoginRequired(loginSuccess = true)
    @ResponseBody
    public String alipay(String outTradeNo,BigDecimal totalAmount,HttpServletRequest request,ModelMap modelMap){
        //获得支付宝请求的客户端（表单请求）
        String form=null;
        AlipayTradePagePayRequest alipayRequest=new AlipayTradePagePayRequest();

        //回调函数
        alipayRequest.setReturnUrl(AlipayConfig.return_payment_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_payment_url);

        Map<String,Object> map = new HashMap<>();
        map.put("out_trade_no",outTradeNo);
        map.put("product_code","FAST_INSTANT_TRADE_PAY");
        map.put("total_amount","0.01");
        map.put("subject","尚硅谷感光徕卡Pro300瞎命名系列手机");
        String param= JSON.toJSONString(map);
        alipayRequest.setBizContent(param);
        try {
            form= alipayClient.pageExecute(alipayRequest).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        //生成并保存用户支付信息
        Order order=orderService.getOrderByOutTradeNo(outTradeNo);
        PaymentInfo paymentInfo=new PaymentInfo();
        paymentInfo.setCreateTime(new Date());
        paymentInfo.setOrderId(order.getId());
        paymentInfo.setOrderSn(outTradeNo);
        paymentInfo.setPaymentStatus("未付款");
        paymentInfo.setSubject("谷粒商城商品一件");
        paymentInfo.setTotalAmount(totalAmount);
        paymentService.savePaymentInfo(paymentInfo);
        // 向消息中间件发送一个检查支付状态(支付服务消费)的延迟消息队列
        paymentService.sendDelayPaymentResultCheckQueue(outTradeNo,5);
        return form;
    }
    @RequestMapping("alipay/callback/return")
    @LoginRequired(loginSuccess = true)
    public String alipayCallbackReturn(HttpServletRequest request,ModelMap modelMap){
        //回调请求中获取支付宝参数
        String sign = request.getParameter("sign");
        String trade_no = request.getParameter("trade_no");
        String out_trade_no = request.getParameter("out_trade_no");
        String trade_status = request.getParameter("trade_status");
        String total_amount = request.getParameter("total_amount");
        String subject = request.getParameter("subject");
        String call_back_content = request.getQueryString();

        // 通过支付宝的paramsMap进行签名验证，2.0版本的接口将paramsMap参数去掉了，导致同步请求没法验签
        if(StringUtils.isNotBlank(sign)){
            // 验签成功
            PaymentInfo paymentInfo = new PaymentInfo();
            paymentInfo.setOrderSn(out_trade_no);
            paymentInfo.setPaymentStatus("已支付");
            paymentInfo.setAlipayTradeNo(trade_no);// 支付宝的交易凭证号
            paymentInfo.setCallbackContent(call_back_content);//回调请求字符串
            paymentInfo.setCallbackTime(new Date());
            // 更新用户的支付状态
            paymentService.updatePayment(paymentInfo);

        }
        return  "finish";
    }
}
