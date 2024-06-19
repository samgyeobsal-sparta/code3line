package sparta.code3line.common;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CommonResponse<T> {
    private String msg;
    private int status;
    private T result;
}
