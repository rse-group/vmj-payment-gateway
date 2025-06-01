package paymentgateway.config.core;

import java.util.Map;

import vmj.routing.route.exceptions.BadRequestException;

public class RequestBodyValidator {
    public static String stringRequestBodyValidator(Map<String, Object> requestBody, String key) {
        if (requestBody.containsKey(key)) {
            String stringField = (String) requestBody.get(key);
            if (stringField.length() == 0) {
                throw new BadRequestException(String.format("%s tidak boleh berupa string kosong.", key));
            } 

            return stringField;
        }
        
        throw new BadRequestException(
            String.format(
                "%s tidak ditemukan pada payload.",
                key
            )
        );
    }

    public static String stringRequestBodyValidator(Map<String, Object> requestBody, String[] keys) {
        for (int i = 0; i < keys.length; i++) {
            if (requestBody.containsKey(keys[i])) {
                String stringField = (String) requestBody.get(keys[i]);
                if (stringField.length() == 0) {
                    throw new BadRequestException(String.format("%s tidak boleh berupa string kosong.", keys[i]));
                } 

            return stringField;
            }
        }

        throw new BadRequestException(
            String.format(
                "%s tidak ditemukan pada payload.",
                String.join(", ", keys)
            )
        );
    }

    public static int intRequestBodyValidator(Map<String, Object> requestBody, String key) {
        if (requestBody.containsKey(key)) {
            try {
                return ((Double) requestBody.get(key)).intValue();
            } catch (ClassCastException e) {
                try {
                    return Double.valueOf((String) requestBody.get(key)).intValue();
                } catch (NumberFormatException ex) {
                    throw new BadRequestException(String.format("%s tidak valid.", key));
                }
            }
        }
        throw new BadRequestException(
            String.format(
                "%s tidak ditemukan pada payload.",
                key
            )
        );
    }

    public static int intRequestBodyValidator(Map<String, Object> requestBody, String[] keys) {
        for (int i = 0; i < keys.length; i++) {
            if (requestBody.containsKey(keys[i])) {
                try {
                    return Integer.parseInt((String) requestBody.get(keys[i]));
                } catch (NumberFormatException e) {
                    try {
                        return Integer.parseInt(String.valueOf((Double) requestBody.get(keys[i])));
                    } catch (Exception ex) {
                        throw new BadRequestException(String.format("%s tidak valid.", keys[i]));
                    }
                }
            }
        }

        throw new BadRequestException(
            String.format(
                "%s tidak ditemukan pada payload.",
                String.join(", ", keys)
            )
        );
    }

    public static double doubleRequestBodyValidator(Map<String, Object> requestBody, String key) {
        if (requestBody.containsKey(key)) {
            Double doubleField;
            try {
                doubleField = ((Double) (requestBody.get(key)));
            } catch (Exception e) {
                try {
                    doubleField = Double.valueOf((String) requestBody.get(key));
                } catch (Exception ex) {
                    throw new BadRequestException(
                        String.format("%s tidak valid.", key)
                    );
                }
            }

            if (doubleField < 0) {
                throw new BadRequestException(String.format("%s tidak boleh negatif.", key));
            }

            return doubleField.doubleValue();
        }

        throw new BadRequestException(
            String.format(
                "%s tidak ditemukan pada payload.",
                key
            )
        );
    }

    public static double doubleRequestBodyValidator(Map<String, Object> requestBody, String[] keys) {
        for (int i = 0; i < keys.length; i++) {
            Double doubleField;
            try {
                doubleField = ((Double) (requestBody.get(keys[i])));
            } catch (Exception e) {
                try {
                    doubleField = Double.valueOf((String) requestBody.get(keys[i]));
                } catch (Exception ex) {
                    throw new BadRequestException(
                        String.format("%s tidak valid.", keys[i])
                    );
                }
            }

            if (doubleField < 0) {
                throw new BadRequestException(String.format("%s tidak boleh negatif.", keys[i]));
            }

            return doubleField.doubleValue();
        }

        throw new BadRequestException(
            String.format(
                "%s tidak ditemukan pada payload.",
                String.join(", ", keys)
            )
        );
    }
}
