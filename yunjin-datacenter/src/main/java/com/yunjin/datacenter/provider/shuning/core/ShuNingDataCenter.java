package com.yunjin.datacenter.provider.shuning.core;

import com.yunjin.datacenter.core.entity.DatacenterMetadataCatalog;
import com.yunjin.datacenter.core.entity.DatacenterMetadataDetail;
import com.yunjin.datacenter.core.instance.AbstractDatacenterInstance;
import com.yunjin.datacenter.provider.shuning.config.ShuNingSupplierInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *   shuning数据平台实现
 * </p>
 *
 * @author Carlos
 * @date 2024-10-10 22:27
 */
@Slf4j
public class ShuNingDataCenter extends AbstractDatacenterInstance<ShuNingSupplierInfo> {


    public ShuNingDataCenter(ShuNingSupplierInfo config) {
        super(config);
    }

    @Override
    public String getSupplier() {
        return ShuNing.SUPPLIER_CODE;
    }

    @Override
    public List<DatacenterMetadataCatalog> metadataList(String keyword) {
        Map<String, Object> map = new HashMap<>(4);
        ShuNing.getClient().get(ShuNing.METADATA_LIST, map);
        return null;
    }

    @Override
    public DatacenterMetadataDetail metadataDetail(String key) {
        return null;
    }

    @Override
    public void data() {

    }

    // @Override
    // public String getSupplier() {
    //     return SmsServiceType.ShuNing.getCode();
    // }
    //
    // @Override
    // public SmsResponse sendMessage(String phone, String message) {
    //     if ("".equals(getConfig().getTemplateId())) {
    //         throw new SmsBlendException("配置文件模板id不能为空！");
    //     }
    //     LinkedHashMap<String, String> map = new LinkedHashMap<>(1);
    //     map.put("code", "1234");
    //     return sendMessage(phone, getConfig().getTemplateId(), map);
    // }
    //
    // @Override
    // public SmsResponse sendMessage(String phone, LinkedHashMap<String, String> messages) {
    //     SmsTemplate currentTemplate = SmsUtil.getCurrentTemplate();
    //     return massTexting(Lists.newArrayList(phone), currentTemplate.getTemplateId(), messages);
    // }
    //
    // /**
    //  * 单条发送
    //  *
    //  * @param phone      参数0
    //  * @param templateId 参数1
    //  * @param messages   参数2
    //  * @return org.dromara.sms4j.api.entity.SmsResponse
    //  * @author Carlos
    //  * @date 2023/11/21 22:24
    //  */
    // @Override
    // public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
    //     // 直接调用群发方式
    //     return massTexting(Lists.newArrayList(phone), templateId, messages);
    // }
    //
    // /**
    //  * 群发短信
    //  *
    //  * @param phones  参数0
    //  * @param message 参数1
    //  * @return org.dromara.sms4j.api.entity.SmsResponse
    //  * @author Carlos
    //  * @date 2023/11/21 22:24
    //  */
    // @Override
    // public SmsResponse massTexting(List<String> phones, String message) {
    //     if ("".equals(getConfig().getTemplateId())) {
    //         throw new SmsBlendException("配置文件模板id不能为空！");
    //     }
    //     LinkedHashMap<String, String> map = new LinkedHashMap<>(1);
    //
    //     return massTexting(phones, getConfig().getTemplateId(), map);
    // }
    //
    // /**
    //  * 群发短信
    //  *
    //  * @param phones     电话号码
    //  * @param templateId 模板id
    //  * @param messages   短信内容，请保持在500字以内。注意：使用短信模板进行提交时，请填写模板拼接后的完整内容。
    //  * @return org.dromara.sms4j.api.entity.SmsResponse
    //  * @author Carlos
    //  * @date 2023/11/21 22:24
    //  */
    // @Override
    // public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
    //     if (phones.size() > DEFAULT_MAX_NUMBER) {
    //         throw new SmsBlendException("单次发送超过最大发送上限，建议每次群发短信人数低于1000");
    //     }
    //     SmsTemplate template = SmsUtil.getCurrentTemplate();
    //     String content = StrUtil.format(template.getContent(), messages);
    //     ShuNingConfig config = getConfig();
    //     ShuNingSendRequest request = new ShuNingSendRequest();
    //     request.setSecretName(config.getAccessKeyId());
    //     request.setSecretKey(config.getAccessKeySecret());
    //     request.setMobile(StrUtil.join(StrUtil.COMMA, phones));
    //     request.setContent(content);
    //     request.setTemplateId(templateId);
    //     String signature = template.getSignature();
    //     if (StrUtil.isBlank(signature)) {
    //         signature = config.getSignature();
    //     }
    //     request.setSignName(signature);
    //     // request.setExtCode("");
    //     // request.setTimeStamp(0L);
    //     // request.setTiming("");
    //     // request.setCustomId("");
    //     return getSmsResponse(BeanUtil.beanToMap(request));
    // }
    //
    //
    // /**
    //  * 获取短信发送响应信息
    //  *
    //  * @param data 请求参数
    //  * @return org.dromara.sms4j.api.entity.SmsResponse
    //  * @author Carlos
    //  * @date 2024/1/15 15:29
    //  */
    // private SmsResponse getSmsResponse(Map<String, Object> data) {
    //     SmsResponse smsResponse = new SmsResponse();
    //     try {
    //         ShuNingResponse response = ShuNing.getClient(getConfig().getRetryInterval(), getConfig().getMaxRetries()).request(ShuNing.SEND_SMS, data);
    //         smsResponse.setSuccess(ShuNingErrorCode.CODE_0.getCode().equals(response.getCode()));
    //         smsResponse.setData(response);
    //         smsResponse.setConfigId(getConfigId());
    //     } catch (Exception e) {
    //         smsResponse.setSuccess(false);
    //     }
    //     return smsResponse;
    // }

}
