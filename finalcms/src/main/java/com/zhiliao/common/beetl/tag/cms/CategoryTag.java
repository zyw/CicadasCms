package com.zhiliao.common.beetl.tag.cms;

import com.google.common.collect.Maps;
import com.zhiliao.common.exception.CmsException;
import com.zhiliao.common.utils.CmsUtil;
import com.zhiliao.common.utils.ControllerUtil;
import com.zhiliao.common.utils.StrUtil;
import com.zhiliao.module.web.cms.service.CategoryService;
import com.zhiliao.mybatis.model.master.TCmsCategory;
import org.beetl.core.GeneralVarTagBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Description:内容列表标签
 *
 * @author Jin
 * @create 2017-05-26
 **/
@Service
@Scope("prototype")
public class CategoryTag extends GeneralVarTagBinding {

	@Autowired
	private CategoryService categoryService;

	@Value("${system.http.protocol}")
	private String httpProtocol;

	@Value("${system.site.subfix}")
	private String siteSubfix;

	@Override
	public void render() {
		Map result = Maps.newHashMap();
		Long categoryId=  (this.getAttributeValue("categoryId") instanceof String)?Long.parseLong((String) this.getAttributeValue("categoryId")):(Long) this.getAttributeValue("categoryId");
		Integer isParent=  (this.getAttributeValue("isParent") instanceof String)?Integer.parseInt((String) this.getAttributeValue("isParent")):(Integer) this.getAttributeValue("categoryId");
		TCmsCategory category = categoryService.findById(categoryId);
		if(CmsUtil.isNullOrEmpty(category))
			throw new CmsException("栏目["+categoryId+"]不存在！");
		if(isParent==1&&category.getParentId()!=0)
			category = categoryService.findById(category.getParentId());
		result.put("categoryId",category.getCategoryId());
		result.put("categoryName",category.getCategoryName());
		result.put("url", !StrUtil.isBlank(category.getUrl())?category.getUrl():httpProtocol + "://" + ControllerUtil.getDomain() + "/front/" + category.getSiteId() + "/" + category.getCategoryId()+siteSubfix);
		result.put("more", !StrUtil.isBlank(category.getUrl())?category.getUrl():httpProtocol + "://" + ControllerUtil.getDomain() + "/front/" + category.getSiteId() + "/" + category.getCategoryId()+"/index_1"+siteSubfix);
		this.binds(result);
		this.doBodyRender();
	}

}
