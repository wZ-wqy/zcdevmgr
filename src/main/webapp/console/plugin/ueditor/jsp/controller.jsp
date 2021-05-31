<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="com.baidu.ueditor.ActionEnter"
    pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%
    request.setCharacterEncoding( "utf-8" );
	response.setHeader("Content-Type" , "text/html");
/* 	String rootPath = application.getRealPath( "/" );
	System.out.println("rootPath:"+rootPath);
	out.write( new ActionEnter( request, rootPath ).exec() ); */
    String rootPath = application.getRealPath( "/" );  
    String action = request.getParameter("action");  
    System.out.println(action+rootPath);
    String result = new ActionEnter( request, rootPath ).exec();  
    System.out.println(result);
    if( action!=null && (action.equals("listfile") || action.equals("listimage")) ){  
       // result = result.replaceFirst("/../", "");//把返回路径中的物理路径替换为 '/'  
    }  
    System.out.println(result);
    out.write( result );  
%>