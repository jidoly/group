package jidoly.group.controller.group;

import lombok.Data;

@Data
public class SearchCondition {

    private String groupName;
    private String info;
    private String orderCondition;

    public SearchCondition(String groupName, String info, String orderCondition) {
        this.groupName = groupName;
        this.info = info;
        this.orderCondition = orderCondition;
    }

    public SearchCondition() {
    }
}
