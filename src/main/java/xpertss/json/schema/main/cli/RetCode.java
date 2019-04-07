package xpertss.json.schema.main.cli;

enum RetCode {
    
    ALL_OK(0),
    CMD_ERROR(2),
    VALIDATION_FAILURE(100),
    SCHEMA_SYNTAX_ERROR(101);

    private final int retCode;

    RetCode(final int retCode)
    {
        this.retCode = retCode;
    }

    int get()
    {
        return retCode;
    }
}
