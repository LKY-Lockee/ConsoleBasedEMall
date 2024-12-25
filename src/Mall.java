import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.Predicate;

public final class Mall {
	public static ArrayList<MenuItem> menuItems = new ArrayList<>() {
		{
			add(new MenuItem("注册", Mall::showSignUp, (condition) -> !UserManager.isLogin()));
			add(new MenuItem("登录", Mall::showLogin, (condition) -> !UserManager.isLogin()));
			add(new MenuItem("查看商城", Mall::showMall,
					(condition) -> !UserManager.isLogin() || !UserManager.getCurrentUser().isAdmin()));
			add(new MenuItem("查看已购", Mall::showBought,
					(condition) -> !UserManager.isLogin() || !UserManager.getCurrentUser().isAdmin()));
			add(new MenuItem("管理员登录", Mall::showAdminLogin, (condition) -> !UserManager.isLogin()));
			add(new MenuItem("查看商品列表", Mall::showAdminProductList,
					(condition) -> UserManager.isLogin() && UserManager.getCurrentUser().isAdmin()));
			add(new MenuItem("添加商品", Mall::showAddProduct,
					(condition) -> UserManager.isLogin() && UserManager.getCurrentUser().isAdmin()));
			add(new MenuItem("修改商品", Mall::showEditProduct,
					(condition) -> UserManager.isLogin() && UserManager.getCurrentUser().isAdmin()));
			add(new MenuItem("删除商品", Mall::showDeleteProduct,
					(condition) -> UserManager.isLogin() && UserManager.getCurrentUser().isAdmin()));
			add(new MenuItem("查看用户列表", Mall::showUserList,
					(condition) -> UserManager.isLogin() && UserManager.getCurrentUser().isAdmin()));
			add(new MenuItem("添加用户", Mall::showAdminAddUser,
					(condition) -> UserManager.isLogin() && UserManager.getCurrentUser().isAdmin()));
			add(new MenuItem("修改用户信息", Mall::showAdminEditUser,
					(condition) -> UserManager.isLogin() && UserManager.getCurrentUser().isAdmin()));
			add(new MenuItem("删除用户", Mall::showAdminDeleteUser,
					(condition) -> UserManager.isLogin() && UserManager.getCurrentUser().isAdmin()));
			add(new MenuItem("查看管理员列表", Mall::showAdminList,
					(condition) -> UserManager.isLogin() && UserManager.getCurrentUser().isAdmin()));
			add(new MenuItem("添加管理员", Mall::showAddAdmin,
					(condition) -> UserManager.isLogin() && UserManager.getCurrentUser().isAdmin()));
			add(new MenuItem("删除管理员", Mall::showDeleteAdmin,
					(condition) -> UserManager.isLogin() && UserManager.getCurrentUser().isAdmin()));
			add(new MenuItem("修改用户名", Mall::showEditUserName, (condition) -> UserManager.isLogin()
					&& !UserManager.getCurrentUser().isAdmin()));
			add(new MenuItem("修改用户名", Mall::showEditAdminName, (condition) -> UserManager.isLogin()
					&& UserManager.getCurrentUser().isAdmin()));
			add(new MenuItem("修改密码", Mall::showEditPassword, (condition) -> UserManager.isLogin()
					&& !UserManager.getCurrentUser().isAdmin()));
			add(new MenuItem("修改密码", Mall::showEditAdminPassword, (condition) -> UserManager.isLogin()
					&& UserManager.getCurrentUser().isAdmin()));
			add(new MenuItem("退出登录", Mall::logOut, (condition) -> UserManager.isLogin()));
			add(new MenuItem("退出商城", Mall::exit, (condition) -> true));
		}
	};

	public static void clear() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	public static void showMainMenu() {
		clear();

		System.out.println("*****欢迎进入电子商城*****");

		for (int i = 0, j = 0; i < menuItems.size(); i++) {
			MenuItem menuItem = menuItems.get(i);
			if (menuItem.canShow()) {
				String menuItemText = (++j >= 10 ? j + ". " : j + ".") + menuItem.name;
				int padding = (16 - menuItem.name.length() - menuItemText.length()) / 2;
				String paddedMenuItemText = "*".repeat(5) + " ".repeat(padding) + menuItemText
						+ " ".repeat(padding) + "*".repeat(5);
				System.out.println(paddedMenuItemText);
			}
		}

		System.out.println("**************************");

		if (UserManager.isLogin()) {
			System.out.print("当前用户: " + UserManager.getCurrentUser().getUserName());
			System.out.println(UserManager.getCurrentUser().isAdmin() ? " (管理员)" : "");
			System.out.println("**************************");
		}

		System.out.print("请选择: ");

		try {
			String i = InputManager.getString();

			int index = Integer.parseInt(i) - 1;
			for (int j = 0; j < menuItems.size(); j++) {
				if (menuItems.get(j).canShow()) {
					if (index == 0) {
						menuItems.get(j).run();
						break;
					}
					index--;
				}
			}
		} catch (NumberFormatException e) {
		} finally {
		}
	}

	public static void showSignUp() {
		clear();

		System.out.println("*********欢迎注册*********");

		String username;

		while (true) {
			System.out.print("请输入用户名，或输入\"/q\"返回: ");
			username = InputManager.getString();
			if (username.equals("/q")) {
				return;
			}

			try {
				ResultSet resultSet = DBManager.executeQuery("SELECT * FROM user WHERE username = '" + username + "'");
				if (resultSet.next()) {
					System.out.println("*******用户名已存在*******");
					continue;
				} else {
					break;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		while (true) {
			System.out.print("请输入密码: ");
			String password = InputManager.getString();
			System.out.print("请再次输入密码: ");
			String passwordValidate = InputManager.getString();

			if (password.equals(passwordValidate)) {
				try {
					if (DBManager.execute(
							"INSERT INTO user (username, password) VALUES ('" + username + "', '" + password + "')")) {
						System.out.println("*********注册成功*********");
						InputManager.pressEnterToContinue();
					}
				} catch (SQLException e) {
					System.out.println("*********注册失败*********");
					e.printStackTrace();
				}
				break;
			} else {
				System.out.println("******两次密码不一致******");
			}
		}
	}

	public static void showLogin() {
		clear();

		System.out.println("*********欢迎登录*********");

		String username;

		while (true) {
			System.out.print("请输入用户名，或输入\"/q\"返回: ");
			username = InputManager.getString();
			if (username.equals("/q")) {
				return;
			}

			try {
				ResultSet resultSet = DBManager.executeQuery("SELECT * FROM user WHERE username = '" + username + "'");
				if (resultSet.next()) {
					break;
				} else {
					System.out.println("*******用户名不存在*******");
					continue;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		while (true) {
			System.out.print("请输入密码: ");
			String password = InputManager.getString();

			try {
				ResultSet resultSet = DBManager.executeQuery(
						"SELECT * FROM user WHERE username = '" + username + "' AND password = '" + password + "'");
				if (resultSet.next()) {
					UserManager.setCurrentUser(username, false);
					System.out.println("*********登录成功*********");
					InputManager.pressEnterToContinue();
					break;
				} else {
					System.out.println("*******密码错误*******");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void showMall() {
		clear();

		System.out.println("************************商城列表************************");

		try {
			ResultSet resultSet = DBManager.executeQuery("SELECT * FROM product");
			System.out.println("商品编号\t商品名称\t商品价格\t商品数量");
			while (resultSet.next()) {
				System.out.print(resultSet.getInt("id"));
				System.out.print("\t\t");
				System.out.print(resultSet.getString("name"));
				System.out.print("\t\t");
				System.out.print(resultSet.getBigDecimal("price"));
				System.out.print("\t\t");
				System.out.print(resultSet.getInt("num"));
				System.out.println();
			}
			System.out.println("********************************************************");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.print("请选择商品编号或按非数字键返回: ");
		String i = InputManager.getString();

		try {
			int id = Integer.parseInt(i);

			if (!UserManager.isLogin()) {
				showLogin();
				showMall();
				return;
			}

			ResultSet resultSet = DBManager.executeQuery("SELECT * FROM product WHERE id = " + id);
			if (resultSet.next()) {
				System.out.print("请输入购买数量: ");
				int num = InputManager.getInt();

				if (resultSet.getInt("num") >= num) {
					UserManager.getCurrentUser()
							.addBought(new ProductData(
									resultSet.getInt("id"),
									resultSet.getString("name"),
									resultSet.getBigDecimal("price"),
									num));
					DBManager.execute("UPDATE product SET num = num - " + num + " WHERE id = " + id);
					System.out.println("************************购买成功************************");
					InputManager.pressEnterToContinue();
					showMall();
				} else {
					System.out.println("************************库存不足************************");
					InputManager.pressEnterToContinue();
					showMall();
				}
			} else {
				System.out.println("***********************商品不存在***********************");
				InputManager.pressEnterToContinue();
				showMall();
			}
		} catch (NumberFormatException e) {
			showMainMenu();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void showBought() {
		clear();

		if (!UserManager.isLogin()) {
			showLogin();
			showBought();
			return;
		}

		System.out.println("****************已购列表****************");

		if (UserManager.isLogin()) {
			UserData currentUser = UserManager.getCurrentUser();
			ArrayList<ProductData> bought = currentUser.getBought();

			System.out.println("商品名称\t商品价格\t商品数量");
			for (ProductData product : bought) {
				System.out.print(product.name);
				System.out.print("\t\t");
				System.out.print(product.price);
				System.out.print("\t\t");
				System.out.print(product.num);
				System.out.println();
			}

			System.out.println("****************************************");
			System.out.println("总价: " + currentUser.getTotalPrice());
			System.out.println("****************************************");
		}

		InputManager.pressEnterToContinue();
	}

	public static void showAdminLogin() {
		clear();

		System.out.println("*********欢迎管理员登录*********");

		String username;

		while (true) {
			System.out.print("请输入用户名，或输入\"/q\"返回: ");
			username = InputManager.getString();
			if (username.equals("/q")) {
				return;
			}

			try {
				ResultSet resultSet = DBManager.executeQuery("SELECT * FROM admin WHERE username = '" + username + "'");
				if (resultSet.next()) {
					break;
				} else {
					System.out.println("**********用户名不存在**********");
					continue;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		while (true) {
			System.out.print("请输入密码: ");
			String password = InputManager.getString();

			try {
				ResultSet resultSet = DBManager.executeQuery(
						"SELECT * FROM admin WHERE username = '" + username + "' AND password = '" + password + "'");
				if (resultSet.next()) {
					UserManager.setCurrentUser(username, true);
					System.out.println("************登录成功************");
					InputManager.pressEnterToContinue();
					break;
				} else {
					System.out.println("************密码错误************");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void showAddProduct() {
		clear();

		System.out.println("*********添加商品*********");

		System.out.print("请输入商品名称，或输入\"/q\"返回: ");
		String name = InputManager.getString();
		if (name.equals("/q")) {
			return;
		}

		System.out.print("请输入商品价格: ");
		BigDecimal price = InputManager.getBigDecimal();

		System.out.print("请输入商品数量: ");
		int num = InputManager.getInt();

		try {
			if (DBManager.execute(
					"INSERT INTO product (name, price, num) VALUES ('" + name + "', " + price + ", " + num + ")")) {
				System.out.println("*********添加成功*********");
			}
		} catch (SQLException e) {
			System.out.println("*********添加失败*********");
		} finally {
			InputManager.pressEnterToContinue();
		}

		showAddProduct();
	}

	public static void showEditProduct() {
		clear();

		System.out.println("************************修改商品************************");

		System.out.print("请输入商品编号或按非数字键返回: ");
		int id;
		try {
			id = InputManager.getInt();
		} catch (NumberFormatException e) {
			showMainMenu();
			return;
		}

		try {
			ResultSet resultSet = DBManager.executeQuery("SELECT * FROM product WHERE id = " + id);
			if (resultSet.next()) {
				System.out.println("************************商品信息************************");

				System.out.println("商品编号\t商品名称\t商品价格\t商品数量");
				System.out.print(resultSet.getInt("id"));
				System.out.print("\t\t");
				System.out.print(resultSet.getString("name"));
				System.out.print("\t\t");
				System.out.print(resultSet.getBigDecimal("price"));
				System.out.print("\t\t");
				System.out.print(resultSet.getInt("num"));
				System.out.println();

				System.out.println("********************************************************");

				System.out.print("请输入商品名称: ");
				String name = InputManager.getString();

				System.out.print("请输入商品价格: ");
				BigDecimal price = InputManager.getBigDecimal();

				System.out.print("请输入商品数量: ");
				int num = InputManager.getInt();

				if (DBManager.execute(
						"UPDATE product SET name = '" + name + "', price = " + price + ", num = " + num + " WHERE id = "
								+ id)) {
					System.out.println("************************修改成功************************");
				}
			} else {
				System.out.println("***********************商品不存在***********************");
			}
		} catch (SQLException e) {
			System.out.println("************************修改失败************************");
		} finally {
			InputManager.pressEnterToContinue();
			showEditProduct();
		}
	}

	public static void showDeleteProduct() {
		clear();

		System.out.println("************************删除商品************************");

		System.out.print("请输入商品编号或按非数字键返回: ");
		int id;
		try {
			id = InputManager.getInt();
		} catch (NumberFormatException e) {
			showMainMenu();
			return;
		}

		try {
			ResultSet resultSet = DBManager.executeQuery("SELECT * FROM product WHERE id = " + id);
			if (resultSet.next()) {
				System.out.println("************************商品信息************************");

				System.out.println("商品编号\t商品名称\t商品价格\t商品数量");
				System.out.print(resultSet.getInt("id"));
				System.out.print("\t\t");
				System.out.print(resultSet.getString("name"));
				System.out.print("\t\t");
				System.out.print(resultSet.getBigDecimal("price"));
				System.out.print("\t\t");
				System.out.print(resultSet.getInt("num"));
				System.out.println();

				System.out.println("********************************************************");

				System.out.print("确认删除该商品吗？(Y/N): ");
				String confirm = InputManager.getString();

				if (confirm.equals("Y") || confirm.equals("y")) {
					if (DBManager.execute("DELETE FROM product WHERE id = " + id)) {
						System.out.println("************************删除成功************************");
					}
				} else if (confirm.equals("N") || confirm.equals("n")) {
					System.out.println("************************取消删除************************");
					InputManager.pressEnterToContinue();
					showDeleteProduct();
					return;
				}
			} else {
				System.out.println("***********************商品不存在***********************");
			}
		} catch (SQLException e) {
			System.out.println("************************删除失败************************");
		} finally {
			InputManager.pressEnterToContinue();
			showDeleteProduct();
		}
	}

	public static void showAdminProductList() {
		clear();

		System.out.println("************************商品列表************************");

		try {
			ResultSet resultSet = DBManager.executeQuery("SELECT * FROM product");
			System.out.println("商品编号\t商品名称\t商品价格\t商品数量");
			while (resultSet.next()) {
				System.out.print(resultSet.getInt("id"));
				System.out.print("\t\t");
				System.out.print(resultSet.getString("name"));
				System.out.print("\t\t");
				System.out.print(resultSet.getBigDecimal("price"));
				System.out.print("\t\t");
				System.out.print(resultSet.getInt("num"));
				System.out.println();
			}
			System.out.println("********************************************************");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		InputManager.pressEnterToContinue();
	}

	public static void showUserList() {
		clear();

		System.out.println("**********用户列表********");

		try {
			ResultSet resultSet = DBManager.executeQuery("SELECT * FROM user");
			System.out.println("用户名");
			while (resultSet.next()) {
				System.out.println(resultSet.getString("username"));
			}
			System.out.println("**************************");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		InputManager.pressEnterToContinue();
	}

	public static void showAdminAddUser() {
		clear();

		System.out.println("*********添加用户*********");

		System.out.print("请输入用户名，或输入\"/q\"返回: ");
		String username = InputManager.getString();
		if (username.equals("/q")) {
			return;
		}

		System.out.print("请输入密码: ");
		String password = InputManager.getString();

		try {
			if (DBManager.execute(
					"INSERT INTO user (username, password) VALUES ('" + username + "', '" + password + "')")) {
				System.out.println("*********添加成功*********");
			}
		} catch (SQLException e) {
			System.out.println("*********添加失败*********");
		} finally {
			InputManager.pressEnterToContinue();
		}

		showAdminAddUser();
	}

	public static void showAdminEditUser() {
		clear();

		System.out.println("*********修改用户信息*********");

		System.out.print("请输入用户名，或输入\"/q\"返回: ");
		String username = InputManager.getString();
		if (username.equals("/q")) {
			return;
		}

		try {
			ResultSet resultSet = DBManager.executeQuery("SELECT * FROM user WHERE username = '" + username + "'");
			if (resultSet.next()) {
				System.out.println("***********用户信息***********");

				System.out.println("用户名\t密码");
				System.out.print(resultSet.getString("username"));
				System.out.print("\t");
				System.out.print(resultSet.getString("password"));
				System.out.println();

				System.out.println("******************************");

				System.out.print("请输入新密码: ");
				String password = InputManager.getString();

				if (DBManager
						.execute("UPDATE user SET password = '" + password + "' WHERE username = '" + username + "'")) {
					System.out.println("***********修改成功***********");
				}
			} else {
				System.out.println("**********用户不存在**********");
			}
		} catch (SQLException e) {
			System.out.println("***********修改失败***********");
		} finally {
			InputManager.pressEnterToContinue();
			showAdminEditUser();
		}
	}

	public static void showAdminDeleteUser() {
		clear();

		System.out.println("*********删除用户*********");

		System.out.print("请输入用户名，或输入\"/q\"返回: ");
		String username = InputManager.getString();
		if (username.equals("/q")) {
			return;
		}

		try {
			ResultSet resultSet = DBManager.executeQuery("SELECT * FROM user WHERE username = '" + username + "'");
			if (resultSet.next()) {
				System.out.println("***********用户信息***********");

				System.out.println("用户名\t密码");
				System.out.print(resultSet.getString("username"));
				System.out.print("\t");
				System.out.print(resultSet.getString("password"));
				System.out.println();

				System.out.println("******************************");

				System.out.print("确认删除该用户吗？(Y/N): ");
				String confirm = InputManager.getString();

				if (confirm.equals("Y") || confirm.equals("y")) {
					if (DBManager.execute("DELETE FROM user WHERE username = '" + username + "'")) {
						System.out.println("***********删除成功***********");
					}
				} else if (confirm.equals("N") || confirm.equals("n")) {
					System.out.println("***********取消删除***********");
					InputManager.pressEnterToContinue();
					showAdminDeleteUser();
					return;
				}
			} else {
				System.out.println("********用户不存在********");
			}
		} catch (SQLException e) {
			System.out.println("***********删除失败***********");
		} finally {
			InputManager.pressEnterToContinue();
			showAdminDeleteUser();
		}
	}

	public static void showAdminList() {
		clear();

		System.out.println("**********管理员列表********");

		try {
			ResultSet resultSet = DBManager.executeQuery("SELECT * FROM admin");
			System.out.println("用户名");
			while (resultSet.next()) {
				System.out.println(resultSet.getString("username"));
			}
			System.out.println("****************************");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		InputManager.pressEnterToContinue();
	}

	public static void showAddAdmin() {
		clear();

		System.out.println("*********添加管理员*********");

		System.out.print("请输入用户名，或输入\"/q\"返回: ");
		String username = InputManager.getString();
		if (username.equals("/q")) {
			return;
		}

		System.out.print("请输入密码: ");
		String password = InputManager.getString();

		try {
			if (DBManager.execute(
					"INSERT INTO admin (username, password) VALUES ('" + username + "', '" + password + "')")) {
				System.out.println("*********添加成功*********");
			}
		} catch (SQLException e) {
			System.out.println("*********添加失败*********");
		} finally {
			InputManager.pressEnterToContinue();
		}

		showAddAdmin();
	}

	public static void showDeleteAdmin() {
		clear();

		System.out.println("*********删除管理员*********");

		System.out.print("请输入用户名，或输入\"/q\"返回: ");
		String username = InputManager.getString();
		if (username.equals("/q")) {
			return;
		}

		try {
			ResultSet resultSet = DBManager.executeQuery("SELECT * FROM admin WHERE username = '" + username + "'");
			if (resultSet.next()) {
				System.out.println("*********管理员信息*********");

				System.out.println("用户名\t密码");
				System.out.print(resultSet.getString("username"));
				System.out.print("\t");
				System.out.print(resultSet.getString("password"));
				System.out.println();

				System.out.println("****************************");

				System.out.print("确认删除该管理员吗？(Y/N): ");
				String confirm = InputManager.getString();

				if (confirm.equals("Y") || confirm.equals("y")) {
					if (DBManager.execute("DELETE FROM admin WHERE username = '" + username + "'")) {
						System.out.println("***********删除成功***********");
					}
				} else if (confirm.equals("N") || confirm.equals("n")) {
					System.out.println("***********取消删除***********");
					InputManager.pressEnterToContinue();
					showDeleteAdmin();
					return;
				}
			} else {
				System.out.println("********管理员不存在********");
			}
		} catch (SQLException e) {
			System.out.println("***********删除失败***********");
		} finally {
			InputManager.pressEnterToContinue();
			showDeleteAdmin();
		}
	}

	public static void showEditUserName() {
		clear();

		System.out.println("*********修改用户名*********");

		System.out.print("请输入新用户名，或输入\"/q\"返回: ");
		String username = InputManager.getString();
		if (username.equals("/q")) {
			return;
		}

		try {
			if (DBManager.execute(
					"UPDATE user SET username = '" + username + "' WHERE username = '"
							+ UserManager.getCurrentUser().getUserName() + "'")) {
				System.out.println("**********修改成功**********");
				logOut();
			}
		} catch (SQLException e) {
			System.out.println("**********修改失败**********");
		} finally {
			InputManager.pressEnterToContinue();
		}
	}

	public static void showEditAdminName() {
		clear();

		System.out.println("*********修改用户名*********");

		System.out.print("请输入新用户名，或输入\"/q\"返回: ");
		String username = InputManager.getString();
		if (username.equals("/q")) {
			return;
		}

		try {
			if (DBManager.execute(
					"UPDATE admin SET username = '" + username + "' WHERE username = '"
							+ UserManager.getCurrentUser().getUserName() + "'")) {
				System.out.println("**********修改成功**********");
				logOut();
			}
		} catch (SQLException e) {
			System.out.println("**********修改失败**********");
		} finally {
			InputManager.pressEnterToContinue();
		}
	}

	public static void showEditPassword() {
		clear();

		System.out.println("*********修改密码*********");

		while (true) {
			System.out.print("请输入原密码，或输入\"/q\"返回: ");
			String password = InputManager.getString();
			if (password.equals("/q")) {
				return;
			}

			try {
				ResultSet resultSet = DBManager.executeQuery(
						"SELECT * FROM user WHERE username = '" + UserManager.getCurrentUser().getUserName()
								+ "' AND password = '" + password + "'");
				if (resultSet.next()) {
					break;
				} else {
					System.out.println("*********密码错误*********");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		while (true) {
			System.out.print("请输入新密码: ");
			String newPassword = InputManager.getString();
			System.out.print("请再次输入密码: ");
			String passwordValidate = InputManager.getString();

			if (newPassword.equals(passwordValidate)) {
				try {
					if (DBManager.execute(
							"UPDATE user SET password = '" + newPassword + "' WHERE username = '"
									+ UserManager.getCurrentUser().getUserName() + "'")) {
						System.out.println("*********修改成功*********");
						logOut();
						return;
					}
				} catch (SQLException e) {
					System.out.println("*********修改失败*********");
				} finally {
					InputManager.pressEnterToContinue();
				}
			} else {
				System.out.println("******两次密码不一致******");
			}
		}
	}

	public static void showEditAdminPassword() {
		clear();

		System.out.println("*********修改密码*********");

		while (true) {
			System.out.print("请输入原密码，或输入\"/q\"返回: ");
			String password = InputManager.getString();
			if (password.equals("/q")) {
				return;
			}

			try {
				ResultSet resultSet = DBManager.executeQuery(
						"SELECT * FROM admin WHERE username = '" + UserManager.getCurrentUser().getUserName()
								+ "' AND password = '" + password + "'");
				if (resultSet.next()) {
					break;
				} else {
					System.out.println("*********密码错误*********");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		while (true) {
			System.out.print("请输入新密码: ");
			String newPassword = InputManager.getString();
			System.out.print("请再次输入密码: ");
			String passwordValidate = InputManager.getString();

			if (newPassword.equals(passwordValidate)) {
				try {
					if (DBManager.execute(
							"UPDATE admin SET password = '" + newPassword + "' WHERE username = '"
									+ UserManager.getCurrentUser().getUserName() + "'")) {
						System.out.println("*********修改成功*********");
						logOut();
						return;
					}
				} catch (SQLException e) {
					System.out.println("*********修改失败*********");
				} finally {
					InputManager.pressEnterToContinue();
				}
			} else {
				System.out.println("******两次密码不一致******");
			}
		}
	}

	public static void logOut() {
		UserManager.logout();
	}

	public static void exit() {
		clear();
		System.exit(0);
	}

	public static class MenuItem {
		private String name;
		private Runnable action;
		private Predicate<Boolean> condition;

		public MenuItem(String name, Runnable action, Predicate<Boolean> condition) {
			this.name = name;
			this.action = action;
			this.condition = condition;
		}

		public boolean canShow() {
			return condition.test(true);
		}

		public void run() {
			action.run();
		}
	}
}
