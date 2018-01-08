package org.security.code;

import org.security.core.validata.code.ValidateCodeGenerator;
import org.security.core.validata.code.image.ImageCode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/*@Component("imageCodeGenerator")*/
public class DemoImageCodeGenerator implements ValidateCodeGenerator {

	@Override
	public ImageCode generate(ServletWebRequest request) {
		 System.out.println("你自己定义的图片验证码器");
		return null;
	}

}
