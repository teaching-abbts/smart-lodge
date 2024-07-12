<html lang="de">
  <body>
    <form action="${login.postUrl}" enctype="application/x-www-form-urlencoded" method="post">
      <p>
        <label for="username">Username:</label>
        <input id="username" type="text" name="${login.userParamName}" />
      </p>
      <p>
        <label for="password">Password:</label>
        <input id="password" type="password" name="${login.passwordParamName}" />
      </p>
      <p>
        <input type="submit" value="Login" />
      </p>
    </form>
  </body>
</html>
