package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.Path;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.MealServiceImpl;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class UserMealServlet extends HttpServlet {

    private static final Logger LOG = getLogger(UserMealServlet.class);

    private MealService mealService = new MealServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("Execute main servlet method");
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        LOG.trace("Extract parameter action: action {}", action);

        if("delete".equalsIgnoreCase(action)){
            Integer id = Integer.valueOf(request.getParameter("id"));
            this.mealService.delete(id);
            LOG.trace("Delete meal by id -> {}", id);
            LOG.debug("Redirect to {}", Path.USER_LIST);
            response.sendRedirect(request.getContextPath().concat(Path.USER_LIST));
        }else{
            String path = Path.USER_LIST_PAGE;
            if(action == null){
                List<MealWithExceed> mealWithExceeds = MealsUtil.getFilteredWithExceeded(this.mealService.findAll(), LocalTime.MIN, LocalTime.MAX, MealsUtil.DEFAULT_CALORIES);
                request.setAttribute("mealsList", mealWithExceeds);
                LOG.trace("Set attribute mealsList: mealWithExceeds {}", mealWithExceeds);
            }else{
                String id = request.getParameter("id");
                Meal meal = id == null ? new Meal(LocalDateTime.now(), "", MealsUtil.DEFAULT_CALORIES) : this.mealService.findById(Integer.valueOf(id));
                request.setAttribute("userMeal", meal);
                path = Path.ADD_OR_EDIT_PAGE;
            }
            LOG.debug("Forward to {}", path);
            request.getRequestDispatcher(path).forward(request,response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        saveOrUpdateProcess(req, resp);
    }

    private void saveOrUpdateProcess(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOG.debug("Execute save or update meal method");
        request.setCharacterEncoding("UTF-8");
        String redirect = Path.USER_LIST;

        Meal meal = new Meal(
                extractId(request),
                LocalDateTime.parse(request.getParameter("dateTime"), DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                request.getParameter("description"),
                Integer.valueOf(request.getParameter("calories"))
        );

        this.mealService.saveOrUpdate(meal);

        LOG.debug("Redirect to {}", redirect);
        response.sendRedirect(request.getContextPath().concat(redirect));
    }

    private Integer extractId(HttpServletRequest req) {
        String id = req.getParameter("id");
        return id != null && !id.isEmpty() ? Integer.valueOf(id) : null;
    }
}
