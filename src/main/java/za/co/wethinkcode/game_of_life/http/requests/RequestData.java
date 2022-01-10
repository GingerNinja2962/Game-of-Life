package za.co.wethinkcode.game_of_life.http.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RequestData {
    private String Name;
    private Integer Size;
    @SuppressWarnings("unused")
    @JsonProperty("state")
    private List<Column> rows;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getSize() {
        return Size;
    }

    public void setSize(Integer size) {
        Size = size;
    }

    static class Column {
        public List<Integer> intList;

        @JsonCreator
        Column(final List<Integer> intList){
            this.intList = intList;
        }
    }
}
