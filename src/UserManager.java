import java.util.HashMap;

public final class UserManager {
	private static UserData currentUser = null;

	private static HashMap<String, UserData> userData = new HashMap<>();

	public static boolean isLogin() {
		return currentUser != null;
	}

	public static UserData getCurrentUser() {
		return currentUser;
	}

	public static void logout() {
		currentUser = null;
	}

	public static void setCurrentUser(String userName, boolean isAdmin) {
		UserData _userData = UserManager.userData.get(userName);
		if (_userData == null) {
			_userData = new UserData(userName, isAdmin);
			userData.put(userName, _userData);
		}
		currentUser = _userData;
	}
}
