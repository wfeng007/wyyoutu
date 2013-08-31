

//request.setAttribute("RESULT_DATA_KEY_RESULT_JSON", result);
request.setAttribute("RESULT_DATA_KEY_REDIRECT_URL", "/index.jsp");//
request.setAttribute("HANDLE_OPTION_KEY_AUTO_DELAY", new Integer(1));//对象必须提供类型，js无法自动转换？
request.setAttribute("RESULT_DATA_KEY_MSG_TEXT", "test result.jsp!!");
request.setAttribute("RESULT_DATA_KEY_ERROR", new NullPointerException("shit!!"));
//request.getRequestDispatcher("/result.jsp").forward(request, response);
request.sendRedirect("/result.jsp");

