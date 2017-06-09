package pw.stamina.mandate.execution.result;

/**
 * @author Mark Johnson
 */
public enum ExitCodes implements ExitCode {
    SUCCESS {
        @Override
        public int getCode() {
            return 0;
        }

        @Override
        public String getIdentifier() {
            return "SUCCESS";
        }
    }, FAILURE {
        @Override
        public int getCode() {
            return 1;
        }

        @Override
        public String getIdentifier() {
            return "FAILURE";
        }
    }, INVALID {
        @Override
        public int getCode() {
            return 2;
        }

        @Override
        public String getIdentifier() {
            return "INVALID";
        }
    }, INTERRUPTED {
        @Override
        public int getCode() {
            return 3;
        }

        @Override
        public String getIdentifier() {
            return "INTERRUPTED";
        }
    }
}
