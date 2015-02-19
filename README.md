# Thymesheet

CSS-inspired Pre-Processing extension to @Thymeleaf which supports CSS3 selectors.  Sometimes referred to as "Decoupled Templates". Separate Thymeleaf from your HTML!

## Maven

Include the latest release from Maven,

		<dependency>
			<groupId>com.connect-group</groupId>
			<artifactId>thymesheet-spring3</artifactId>
			<version>2.1.2</version>
		</dependency>



## What is Thymeleaf?
Thymeleaf is a Java library. It is an XML / XHTML / HTML5 template engine (extensible to other formats) that can work both in web and non-web environments, best suited for serving XHTML/HTML5.

It provides an optional module for integration with Spring MVC, so that you can use it as a complete substitute of JSP in your applications made with this technology, even with HTML5.

The main goal of Thymeleaf is to provide an elegant and well-formed way of creating powerful natural templates that can be correctly displayed by browsers and therefore work also as static prototypes. 

For more detail, see http://www.thymeleaf.org/

### What does Thymeleaf look like?
It looks like this:

    <table>
      <thead>
        <tr>
          <th th:text="#{msgs.headers.name}">Name</th>
          <th th:text="#{msgs.headers.price}">Price</th>
        </tr>
      </thead>
      <tbody>
        <tr th:each="prod : ${allProducts}">
          <td th:text="${prod.name}">Oranges</td>
          <td th:text="${#numbers.formatDecimal(prod.price,1,2)}">0.99</td>
        </tr>
      </tbody>
    </table>

## What is Thymesheet?
When you develop HTML, it is good practice to place styling information outside of the HTML itself.
This is done by adding a CSS "cascading style sheet", which tells the web browser how to style the HTML document.

Thymesheet applies the same idea to Thymeleaf.  Add a link to an external Thymesheet file (or files), then remove the Thymesheet syntax from the HTML.

### What does Thymesheet look like?
The HTML looks like this,

    <head>
        <link rel="thymesheet" href="/thymesheet.ts"/>
    <head>
    <body>
      <table>
        <thead>
          <tr>
            <th>Name</th>
            <th>Price</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>Oranges</td>
            <td>0.99</td>
          </tr>
      </tbody>
    </table>
    </body>

The Thymesheet "thymesheet.ts" file looks like this,

    table > thead > tr th:first-child {
        th-text: "#{msgs.headers.name}"
    }
    
    table > thead > tr th:nth-child(2) {
        th-text: "#{msgs.headers.price}"
    }
    
    table > tbody > tr {
	    th-each:"prod : ${allProducts}";
    }
    
    table > tbody > tr > td:first-child {
        th-text: "${prod.name}"
    }
    
    table > tbody > tr > td:nth-child(2) {
        th-text: "${#numbers.formatDecimal(prod.price,1,2)}"
    }

    
## Sample Code
A sample application can be found at https://github.com/connect-group/thymesheet-sample

## Spring Integration
Two separate Spring project integrates Thymesheet (and Thymeleaf) with Spring 3/4.
The source for these can be seen at https://github.com/connect-group/thymesheet-spring3 and https://github.com/connect-group/thymesheet-spring3
See also https://github.com/connect-group/thymesheet/wiki/Hosting-TSS-Files-in-the-webapp-or-WEB-INF for information about using Spring.

### Spring 3 Integration

		<dependency>
			<groupId>com.connect-group</groupId>
			<artifactId>thymesheet-spring3</artifactId>
			<version>2.1.2</version>
		</dependency>
		
### Spring 4 Integration

		<dependency>
			<groupId>com.connect-group</groupId>
			<artifactId>thymesheet-spring3</artifactId>
			<version>2.1.2</version>
		</dependency>

## More information
More information can be found on the [Wiki](https://github.com/connect-group/thymesheet/wiki).

## Additional Credits
- Thymesheet is an extension for [Thymeleaf](http://www.thymeleaf.org/) v2.1 which is Apache 2 licensed.
- It has one other dependancy, [cssparser](http://cssparser.sourceforge.net/) which is LGPL licensed.
- Additionally, the project uses a modified version of [css-selectors](https://github.com/chrsan/css-selectors) by Christer Sandberg, which is Apache 2 licensed.
