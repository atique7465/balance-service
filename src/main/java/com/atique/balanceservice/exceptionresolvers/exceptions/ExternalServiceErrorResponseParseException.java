package com.atique.balanceservice.exceptionresolvers.exceptions;

import com.atique.balanceservice.exceptionresolvers.enums.ErrorCode;

/**
 * @author atiQue
 * @since 19'Jun 2023 at 12:36 AM
 */

public class ExternalServiceErrorResponseParseException extends BaseException{

    public ExternalServiceErrorResponseParseException(){
        super(ErrorCode.EXT_SERVICE_ERR_RES_PARSE_ERROR);
    }

    public ExternalServiceErrorResponseParseException(ErrorCode errorCode){
        super(errorCode);
    }
}
