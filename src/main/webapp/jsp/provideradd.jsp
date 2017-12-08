<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/jsp/common/head.jsp"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="fm" %>
<div class="right">
        <div class="location">
            <strong>你现在所在的位置是:</strong>
            <span>供应商管理页面 >> 供应商添加页面</span>
        </div>
        <div class="providerAdd">
           <%-- <form id="providerForm" name="providerForm" method="post" action="${pageContext.request.contextPath }/provider/addProviders.do"> --%>
			<fm:form modelAttribute="provider" id="providerForm" method="post" action="${pageContext.request.contextPath }/provider/addProviders.do"
				enctype="multipart/form-data">
                <!--div的class 为error是验证错误，ok是验证成功-->
                <div class="">
                    <label for="proCode">供应商编码：</label>
                    <!-- <input type="text" name="proCode" id="proCode" value="">  -->
                    <fm:input path="proCode"/>
					<!-- 放置提示信息 -->
					<font color="red"></font>
                </div>
                <div>
                    <label for="proName">供应商名称：</label>
                    <!-- <input type="text" name="proName" id="proName" value="">  -->
                    <fm:input path="proName"/>
					<font color="red"></font>
                </div>
                <div>
                    <label for="proContact">联系人：</label>
                    <!-- <input type="text" name="proContact" id="proContact" value="">  -->
                    <fm:input path="proContact"/>
					<font color="red"></font>

                </div>
                <div>
                    <label for="proPhone">联系电话：</label>
                    <!-- <input type="text" name="proPhone" id="proPhone" value="">  -->
                    <fm:input path="proPhone"/>
					<font color="red"></font>
                </div>
                <div>
                    <label for="proAddress">联系地址：</label>
                    <!-- <input type="text" name="proAddress" id="proAddress" value="">  -->
                    <fm:input path="proAddress"/>
                </div>
                <div>
                    <label for="proFax">传真：</label>
                    <!-- <input type="text" name="proFax" id="proFax" value="">  -->
                    <fm:input path="proFax"/>
                </div>
                <div>
                    <label for="proDesc">描述：</label>
                    <!-- <input type="text" name="proDesc" id="proDesc" value="">  -->
                    <fm:input path="proDesc"/>
                </div>
                
                <div>
                    <label for="proDesc">执照：</label>
                    <input type="file" name="licenses" id="license" value=""> 
                </div>
                <div>
                    <label for="proDesc">正面组织机构：</label>
                    <input type="file" name="licenses" id="frontCode" value=""> 
                </div>
                <div>
                    <label for="proDesc">反面组织机构：</label>
                    <input type="file" name="licenses" id="sideCode" value=""> 
                </div>
                
                <div class="providerAddBtn">
                    <input type="button" name="add" id="add" value="保存">
					<input type="button" id="back" name="back" value="返回" >
                </div>
            </fm:form>
     </div>
</div>
</section>
<%@include file="/jsp/common/foot.jsp" %>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/provideradd.js"></script>
