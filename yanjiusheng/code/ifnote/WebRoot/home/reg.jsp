<%@ page language="java" pageEncoding="GB18030"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
 
<html> 
	<head>
		<title>ע��</title>
	</head>
	<body>
		<html:form action="/reg" styleId="regForm">
        <table width="100%" border="0">
  <tr>
    <td align="center"><font face="΢���ź�" size="5" color="#0000ff">����ע��</font></td>
  </tr>
  <tr>
    <td><table width="100%" border="0">
  <tr>
    <td><div align="right">�û�����</div></td>
    <td><div align="left">
      <html:text property="username"/>
      <html:errors property="username"/>
    </div></td>
  </tr>
  <tr>
    <td><div align="right">���룺</div></td>
    <td><div align="left">
      <html:password property="psw"/>
      <html:errors property="psw"/>
    </div></td>
  </tr>
  <tr>
    <td><div align="right">ȷ�����룺</div></td>
    <td><div align="left">
      <html:password property="psw2"/>
      <html:errors property="psw2"/>
    </div></td>
  </tr>
  <tr>
    <td><div align="right">Email��</div></td>
    <td><div align="left">
      <html:text property="email"/>
      <html:errors property="email"/>
    </div></td>
  </tr>
  <tr>
    <td><div align="right">�ֻ���</div></td>
    <td><div align="left">
      <html:text property="phone"/>
      <html:errors property="phone"/>
    </div></td>
  </tr>
</table>
</td>
  </tr>
  <tr>
    <td align="center">ͬ��������<img src="/ifnote/images/submit.png" width="72" height="34" alt="�ύ" style="cursor:hand;" 
    onClick="javascript:document.regForm.submit();"></td>
  </tr>
</table>
		</html:form>
	</body>
</html>

