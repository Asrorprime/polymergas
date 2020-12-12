package uz.ecma.apppolymergasserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.ecma.apppolymergasserver.payload.ApiResponseModel;
import uz.ecma.apppolymergasserver.payload.ReqDashboard;
import uz.ecma.apppolymergasserver.payload.ResDashboard;
import uz.ecma.apppolymergasserver.repository.UserRepository;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductService productService;

    @Autowired
    UserOrderService userOrderService;


    public ApiResponseModel getDashboard(ReqDashboard reqDashboard) {
        Timestamp from = Timestamp.valueOf(reqDashboard.getFrom());
        Timestamp to = Timestamp.valueOf(reqDashboard.getTo());
        Integer usersCount = userRepository.countAllByCreatedAtBetween(from, to);
        Map<Integer, Integer> userStatistic = new HashMap<>();
        if (reqDashboard.getType().equals("month")) {
            do {

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(from.getTime()));
                calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
                Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
                userStatistic.put(calendar.get(Calendar.MONTH), userRepository.countAllByCreatedAtBetween(from, timestamp));
                from.setTime(calendar.getTimeInMillis());
            }
            while (!from.after(to));
        } else if (reqDashboard.getType().equals("hour")) {
            do {

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(from.getTime()));
                calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
                userStatistic.put(calendar.get(Calendar.DAY_OF_MONTH), userRepository.countAllByCreatedAtBetween(from, new Timestamp(calendar.getTimeInMillis())));
                from.setTime(calendar.getTimeInMillis());
            }
            while (!from.after(to));
        } else {
            do {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(from.getTime()));
                calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
                userStatistic.put(calendar.get(Calendar.YEAR), userRepository.countAllByCreatedAtBetween(from, new Timestamp(calendar.getTimeInMillis())));

                from.setTime(calendar.getTimeInMillis());
            }
            while (from.getYear() < to.getYear());
        }
        ResDashboard dashboardProduct = productService.getProductToDashboard(reqDashboard);
        ResDashboard dashboardService = userOrderService.getDashboardUserOrder(reqDashboard);
        return new ApiResponseModel(true, "Dashboard", new ResDashboard(usersCount, userStatistic, dashboardProduct.getProductCount(), dashboardProduct.getProductStatistic(), dashboardService.getAborotCount(), dashboardService.getAborotSum(), dashboardProduct.getTop10Product(),dashboardService.getAbrotStatistic()));
    }
}
