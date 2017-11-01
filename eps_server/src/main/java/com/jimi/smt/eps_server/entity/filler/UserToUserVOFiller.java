package com.jimi.smt.eps_server.entity.filler;

import java.text.SimpleDateFormat;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.jimi.smt.eps_server.entity.User;
import com.jimi.smt.eps_server.entity.vo.UserVO;
import com.jimi.smt.eps_server.util.VoFieldFiller;

@Component
public class UserToUserVOFiller extends VoFieldFiller<User, UserVO> {

	@Override
	public UserVO fill(User user) {
		UserVO userVO = new UserVO();
		BeanUtils.copyProperties(user, userVO);
		userVO.setPassword("***");
		switch (userVO.getType()) {
		case 0:
			userVO.setTypeName("仓库操作员");
			break;
		case 1:
			userVO.setTypeName("厂线操作员");
			break;
		case 2:
			userVO.setTypeName("IPQC");
			break;
		case 3:
			userVO.setTypeName("管理员");
			break;
		default:
			break;
		}
		if(userVO.getClassType() == 0) {
			userVO.setClassTypeName("白班");
		}else {
			userVO.setClassTypeName("夜班");
		}
		if(userVO.getEnabled() == true) {
			userVO.setIsEnabled("是");
		}else {
			userVO.setIsEnabled("否");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		userVO.setCreateTimeString(sdf.format(userVO.getCreateTime()));
		return userVO;
	}

}
