package rut.pan.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import rut.pan.entity.TaskType;

@AllArgsConstructor
@Getter
public enum TaskEnum {

    STORY("Story", "Описание", "#00FFFF", ""),
    TASK("Task", "Задача", "#0000FF", ""),
    BUG("Bug", "Баг", "#DC143C", ""),
    SUBTASK("Subtask", "Подзадача", "FF0000", ""),
    TEST("Test", "Тест", "#008000", "");

    private final String tag;
    private final String name;
    private final String color;
    private final String description;

    public static String getColorByType(String type) {
        for (TaskEnum task : TaskEnum.values()) {
            if (type.equalsIgnoreCase(task.tag))
                return task.color;
        }
        return "#008000"; //как заплатка навсякий
    }

    public static String getTagByColor(String color) {
        for (TaskEnum task : TaskEnum.values()) {
            if (color.equalsIgnoreCase(task.color))
                return task.tag;
        }
        return "Task"; //переписать
    }

}
