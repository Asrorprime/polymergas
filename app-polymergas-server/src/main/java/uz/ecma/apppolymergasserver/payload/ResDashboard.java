package uz.ecma.apppolymergasserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResDashboard {
    private Integer usersCount;
    private Map<Integer, Integer> usersStatistic;
    private Integer productCount;
    private Map<Integer, Integer> productStatistic;
    private Integer aborotCount;
    private Double aborotSum;
    private List<Object[]> top10Product;

    public ResDashboard(Integer productCount, Map<Integer, Integer> productStatistic, List<Object[]> top10Product) {
        this.productCount = productCount;
        this.productStatistic = productStatistic;
        this.top10Product = top10Product;
    }

    public ResDashboard(Integer productCount, Map<Integer, Integer> productStatistic, Double aborotSum) {
        this.productCount = productCount;
        this.productStatistic = productStatistic;
        this.aborotSum = aborotSum;
    }

    private Map<Integer, Integer> abrotStatistic;

}
