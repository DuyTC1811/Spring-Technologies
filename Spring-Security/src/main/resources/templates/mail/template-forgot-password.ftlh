<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Template Example email</title>
    <base href="/">
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>
<#if name??>
    <h3>Hello, ${email}!</h3>
</#if>
<p>This is a test email sent from a Spring Boot application using FreeMarker.</p>
<a href="${baseUrl}/api/auth/validate-token?token=${token}">Click here to validate your token</a>
<p>Thank you!</p>
</body>
</html>
