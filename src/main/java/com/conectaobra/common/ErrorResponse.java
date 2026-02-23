package com.conectaobra.common;

import java.util.List;

public record ErrorResponse (int status, List<ErrorFields> erros){
}
