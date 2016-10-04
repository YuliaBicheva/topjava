package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.TimeUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * User: yulia
 * Date: 04.10.2016
 */
public class MealServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(MealServlet.class);

    private ConfigurableApplicationContext ctx;
    private MealRestController controller;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ctx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        controller = ctx.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
//        ctx.close();
        super.destroy();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if(action == null) {
            String id = request.getParameter("id");

            Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                    LocalDateTime.parse(request.getParameter("dateTime"), TimeUtil.DATE_TIME_FORMATTER),
                    request.getParameter("description"),
                    Integer.valueOf(request.getParameter("calories")));

            LOG.info(meal.isNew() ? "Create {}" : "Update {}", meal);
            controller.save(meal);
            response.sendRedirect("meals");
        }else if(action.equalsIgnoreCase("filter")){
            String param = resetParam("startDate",request);
            LocalDate startDate = param == null || param.isEmpty() ? LocalDate.MIN : TimeUtil.parseLocaleDate(param);
            param = resetParam("endDate",request);
            LocalDate endDate = param == null || param.isEmpty() ? LocalDate.MAX : TimeUtil.parseLocaleDate(param);
            param = resetParam("startTime",request);
            LocalTime startTime = param == null || param.isEmpty() ? LocalTime.MIN : TimeUtil.parseLocaleTime(param);
            param = resetParam("endTime",request);
            LocalTime endTime = param == null || param.isEmpty() ? LocalTime.MAX : TimeUtil.parseLocaleTime(param);
            request.setAttribute("mealList", controller.getBetween(startDate, startTime, endDate, endTime));
            request.getRequestDispatcher("/mealList.jsp").forward(request, response);
        }
    }

    private String resetParam(String param, HttpServletRequest request) {
        String value = request.getParameter(param);
        request.setAttribute(param, value);
        return value;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            LOG.info("getAll");
            request.setAttribute("mealList", controller.getAll());
            request.getRequestDispatcher("/mealList.jsp").forward(request, response);
        } else if ("delete".equals(action)) {
            int id = getId(request);
            LOG.info("Delete {}", id);
            controller.delete(id);
            response.sendRedirect("meals");
        } else if ("create".equals(action) || "update".equals(action)) {
            final Meal meal = action.equals("create") ?
                    new Meal(LocalDateTime.now().withNano(0).withSecond(0), "", 1000) :
                    controller.get(getId(request));
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("mealEdit.jsp").forward(request, response);
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }
}
