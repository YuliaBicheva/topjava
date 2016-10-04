package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.web.user.AdminRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * User: gkislin
 * Date: 19.08.2014
 */
public class UserServlet extends HttpServlet {
    private static final Logger LOG = getLogger(UserServlet.class);

    private ConfigurableApplicationContext ctx;
    private AdminRestController controller;

    @Override
    public void init() throws ServletException {
        super.init();
        ctx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        controller = ctx.getBean(AdminRestController.class);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("forward to userList");
        request.setAttribute("userList", controller.getAll());
        request.getRequestDispatcher("/userList.jsp").forward(request, response);
    }
}
