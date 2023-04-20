package com.shop.admin.paging;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PagingAndSortingArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(PagingAndSortingParam.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer model, NativeWebRequest request, WebDataBinderFactory factory) {
        var keyword = request.getParameter("keyword");
        var sortField = request.getParameter("sortField");
        var sortDir = request.getParameter("sortDir");
        var reverseSortOrder = sortDir.equals("asc") ? "desc" : "asc";
        var annotation = parameter.getParameterAnnotation(PagingAndSortingParam.class);
        var listName = annotation.listName();
        var moduleURL = annotation.moduleURL();

        model.addAttribute("sortDir", sortDir);
        model.addAttribute("sortField", sortField);
        model.addAttribute("keyword", keyword);
        model.addAttribute("reverseSortOrder", reverseSortOrder);
        model.addAttribute("moduleURL", moduleURL);

        return new PagingAndSortingHelper(model, listName, sortField, sortDir, keyword);
    }
}
