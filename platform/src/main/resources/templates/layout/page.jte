@import gg.jte.Content

@param Content sidebar = null
@param Content content
@param Content footer = null

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" href="/static/css/tailwind.css"/>
</head>
<body>
@if (sidebar != null)
    ${sidebar}
@endif
${content}
@if (footer != null)
    ${footer}
@endif
</body>
</html>
