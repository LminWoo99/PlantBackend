package Plant.PlantProject.common.util;

import com.querydsl.core.types.Order;
import lombok.Data;
import lombok.Getter;

@Getter
public enum SearchParam {
    MANAGE("manage"),
    CATEGORY("category"),
    TITLE("title"),
    KEYWORD("keyword");





    private final String paramKey;

    SearchParam(String paramKey) {
        this.paramKey = paramKey;
    }

}
