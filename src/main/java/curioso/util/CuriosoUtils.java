package curioso.util;

import java.util.Objects;
import java.util.stream.Stream;

public class CuriosoUtils {
	
	/**
	 * Check if the object params are not null.
	 * 
	 * @param objects to check if it is null.
	 * @return true if all the parameters are not null, false otherwise.
	 */
	public static boolean allNotNull(Object... objects) {
		return objects != null && Stream.of(objects).allMatch(Objects::nonNull);
	}

	/**
	 * Check if the object params are not empty or null.
	 * 
	 * @param strings to check if it is null or empty.
	 * @return true if all the parameters are not empty, false otherwise.
	 */
	public static boolean allNotEmpty(String... strings) {
		return strings != null && Stream.of(strings).allMatch(str -> str != null && !str.isBlank());
	}
	
}
