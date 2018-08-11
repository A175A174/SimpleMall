package entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@ToString(of = "age")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"sex","name"})
@Slf4j
public class People {
    private String name;
    private String age;
    private String sex;

    public static void main(String[] args) {
        People people = new People();
        people.setAge("asdas");
        System.out.println(people);
        log.info("hahahahaha");
    }
}
