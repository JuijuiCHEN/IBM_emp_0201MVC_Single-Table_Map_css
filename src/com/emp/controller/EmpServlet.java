package com.emp.controller;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.emp.model.*;

public class EmpServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");

        if ("insert".equals(action)) { // �Ӧ�addEmp.jsp���ШD  
			
			Map<String,String> errorMsgs = new LinkedHashMap<String,String>();
			req.setAttribute("errorMsgs", errorMsgs);

			try {
				/***********************1.�����ШD�Ѽ� - ��J�榡�����~�B�z*************************/
				String ename = req.getParameter("ename");
				String enameReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$";
				if (ename == null || ename.trim().length() == 0) {
					errorMsgs.put("ename","���u�m�W: �ФŪť�");
				} else if(!ename.trim().matches(enameReg)) { //�H�U�m�ߥ��h(�W)��ܦ�(regular-expression)
					errorMsgs.put("ename","���u�m�W: �u��O���B�^��r���B�Ʀr�M_ , �B���ץ��ݦb2��10����");
	            }
				
				String job = req.getParameter("job").trim();
				if (job == null || job.trim().length() == 0) {
					errorMsgs.put("job","¾��ФŪť�");
				}
				
				java.sql.Date hiredate = null;
				try {
					hiredate = java.sql.Date.valueOf(req.getParameter("hiredate").trim());
				} catch (IllegalArgumentException e) {
					errorMsgs.put("hiredate","�п�J���");
				}
				
				Double sal = null;
				try {
					sal = new Double(req.getParameter("sal").trim());
				} catch (NumberFormatException e) {
					errorMsgs.put("sal","�~���ж�Ʀr");
				}
				
				Double comm = null;
				try {
					comm = new Double(req.getParameter("comm").trim());
				} catch (NumberFormatException e) {
					errorMsgs.put("comm","�����ж�Ʀr");
				}
				
				Integer deptno = new Integer(req.getParameter("deptno").trim());

				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/emp/addEmp.jsp");
					failureView.forward(req, res);
					return;
				}
				
				/***************************2.�}�l�s�W���***************************************/
				EmpService empSvc = new EmpService();
				empSvc.addEmp(ename, job, hiredate, sal, comm, deptno);
				
				/***************************3.�s�W����,�ǳ����(Send the Success view)***********/
				String url = "/emp/listAllEmp.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // �s�W���\�����listAllEmp.jsp
				successView.forward(req, res);				
				
				/***************************��L�i�઺���~�B�z**********************************/
			} catch (Exception e) {
				errorMsgs.put("Exception",e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/emp/addEmp.jsp");
				failureView.forward(req, res);
			}
		}
	}
}
