package com.conectaobra.common;

import java.util.List;

public record ErrorResponse (int status, String mensagem, List<ErrorFields> erros){
}
