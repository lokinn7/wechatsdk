package com.riversoft.weixin.app;

import com.riversoft.weixin.app.user.MAppuserinfo;
import com.riversoft.weixin.app.user.WeRuninfo;
import com.riversoft.weixin.common.util.JsonMapper;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;

/**
 * @author liuyijie
 */
public class MAppUtils {

    private static Logger logger = LoggerFactory.getLogger(MAppUtils.class);


    /**
     * 解密用户敏感数据获取用户信息
     *
     * @param sessionKey 数据进行加密签名的密钥
     * @param encryptedData 包括敏感数据在内的完整用户信息的加密数据
     * @param iv 加密算法的初始向量
     * @return
     */
    public static MAppuserinfo getUserInfo(String encryptedData, String sessionKey, String iv){
        return JsonMapper.nonEmptyMapper().fromJson(des(encryptedData, sessionKey, iv), MAppuserinfo.class);
    }

    /**
     * 解密用户微信运动
     *
     * @param sessionKey 数据进行加密签名的密钥
     * @param encryptedData 包括敏感数据在内的完整用户信息的加密数据
     * @param iv 加密算法的初始向量
     * @return
     */
    public static WeRuninfo getWeRun(String encryptedData, String sessionKey, String iv){
        return JsonMapper.nonEmptyMapper().fromJson(des(encryptedData, sessionKey, iv), WeRuninfo.class);
    }

    /**
     * 解密用户敏感数据获取用户信息
     *
     * @param sessionKey 数据进行加密签名的密钥
     * @param encryptedData 包括敏感数据在内的完整用户信息的加密数据
     * @param iv 加密算法的初始向量
     * @return
     */
    public static String des(String encryptedData, String sessionKey, String iv){
        try {
            // 被加密的数据
            byte[] dataByte = Base64.decode(encryptedData);
            // 加密秘钥
            byte[] keyByte = Base64.decode(sessionKey);
            // 偏移量
            byte[] ivByte = Base64.decode(iv);
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            // 初始化
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return result;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        main1();
        String se = "ngM/ZeEfg5UEJbx8jmZuloxFuKHL2QZwuR+M4q0tlMNtUCzlMqPx2Mca6c2fK/VeaxXniZqcQj2Ay9iVgtngkqt2/AeuofkA7sWL272CQIHxr+jaHJo7Oo4IyqjgrYhfL8h6L4w/RBIMMwI4LEr61IunSPHTmUlKXzDaMBIuS36XdtM1RA7LjuLLiGhjMlreCiIHSDHmNQvAom3kpjeaWpsPukIUGE+Ui6zvdBJcerDj/tvYepoiCzQDgM3+vaIzTxD3x8nogcExyn71ClSnIp6CuqQUsogL+DM8zLt5HQI47yy9FJ3fmfZPz3trBmBpZZPmhn43aJ/7hOUZDCC0c2FhMq1ckBEPOSfLkiIlGHZXNNqKQ15s5Zy4/p65s4DN8t2qzIwkdz/QeoCeHQREfKihTq38WfNBwqQWdjGFzmg+3jOb5NJabduxGTSahGAwLqjloc2+RZE1gr+cbxSUQgiS9oeQJhBW6cnkmwH2kfaNzESlrCNJPEufMZ4eNffQJTWWYTZY5sgrvqE9RIUnbYFCzFL06UiTIfHykiASdallxmP3UB60X41SrIqh6J7z7kZSaL+3ZeSPBB3+2SsXmDUbq8AF1bmhi4v9dUKpwhV5JccWDuHhMNyvUsoXAjDo2j40KGRNEgLL9aZ/uExHVM0L2jVXv4hLsbNWZAxiurBLLDfxtTHzrAR2ztLHIMOzrS5RhlVf/6iWkD3YEFjZGwTfUtj3LoGvhARbdYrtnnfNXNof6CLqliEpEUpZ59nP+GAlM93c8MfwjDnSLMbVdI1DA7pJO5SI06JwdNYaUyRNU4Ll/rEMdOFC0QvqR+0xwa5/qYszbgebxsSijAmNGsHO/b94k8ngWP4fONltRteW0qGPi2zH3xAMCHq8qf2qx/v5mfCqfJ2Zm4t7Tejh793lu11qnFfyey1ZkTeUU95kJlUU0czWpPpv+p8dw/waEXlugpVKMrrjVKTGPmq7iiJt6Fl7+cwXgPOqsDUvQlC4Y1+G6od+4uQKxf2a+yYdrDTW7dHZWwkozzPOMd5oVsVabmvlrDAUK6w0d0s3x5xPsP2OX9OQNUqXw8KzywQXfUqVGFu5j4wKtOAf2I8d9XVp7dYxDhxVcayG6wK/nulo2lvGL06nOHXICAjynwRW+SoqldQ75lDNfaV6fE/SUrh0MECvwZp2v8708cJ8cfZbdPSb3HbIOrDn1p0uE73tYmgC6MoEHMhhbcrtkLmmSOKm0l6dYrrhYobn74iosWA7LvqG/NrbOcndFQTScey9p20nwo4BRmF+aqtlQRknNwsfq6vezgCy2f2jkJQ+N6wEdADvCc/1pC1YLbmrBS1Q5z41ZwKcpK/VartpezbuQpDMn6u9vtl45x9QjI47e5LxDIKvfTpY7O+zUaMp+LswSFcuuyXdrlV3O4B7sby5XXcxLROOljaOlqwizH+FAgI57g23SI+29rj5pWcM84/9/cUuFZHbhcMHYiRnr7Y3QWCVR5wYGFOvcz7iRwJG9if9W0Rtk8x98Ot3X21rUzKkQ1MQrwsD4vdi0QzoUF27wtFZWa2LC5X0E7lHyPyuwiogwYT/QvkZtFgzMcSrSC/UDtgbpfnO0DGOcdNaxOYQkg+grv4NdNha6APRAzYDegjTwvlLwjC8xKl3D7ApFAE/";
        WeRuninfo str = getWeRun(se, "/OZnxrEivBfu9ygcROr33A==", "ZmzXj+bGPemyOmDNB7ew0A==");
        System.out.println(str.getStepInfoList().get(0).getTimestamp());
    }

    public static void main1() {
        String se = "0OJUi2FHAusI7ol4QFGvX3imBzHj0h3J20MwC92cV98IOkjDe7GlMFT5uGAlXHkDvy+qkhnApyp1xQwRQqHExCuP6UbRWaXPNmnm0WdqFlp9q1pBbAROk+70thRovxWd11rOYhQ3AaGWilTfM2Qz18Atx4NULXglNAZhMvMoaA3GXRdmwzefm3zr1a/AjB5kj5elcdfGJFIR+e4lsJ0q28IvyINcvjr2FfhMdclPtNqACOKlM9JP8QsIdAW/8xlRHALGIFNex+XGOGI6M7CdyThcL2Jn4b5KVqWypUYmKf+zRHxEGcG/XshgMGNdrNOcEUTDgVdM09vzZmJc/O5aKY9tRy2v5HKPnuGph+O4bM6QSJTtGz8M0M2zBo6xCdOTiWxcl/64WDQYkZzVW04s+RlyGLWG1y5GLBxK2gxkv7XABLlUrDqsU+9KjRCMRx4a7woq99hknx3OBJSM3xvIehMl4KDdwlMKkEiPUP4uScA=";
        MAppuserinfo userInfo = MAppUtils.getUserInfo(se, "pyVERBwwv32yiKv5hDWGHw==", "Qh5WfFEtXikWDkz82GpzvQ==");
        System.out.println(userInfo.getAvatarUrl());

    }
}
