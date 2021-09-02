package Model;

import lombok.Data;

@Data
public class ComparableModel implements Comparable<ComparableModel>{

    private Integer age;
    private String name;




    @Override
    public int compareTo(ComparableModel o) {
        if(o==null)
        {
            return  1;
        }
        else {
          return this.age.compareTo(o.age);
        }
    }
}
