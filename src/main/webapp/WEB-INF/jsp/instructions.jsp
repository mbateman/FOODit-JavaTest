<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" uri="http://threewks.com/thundr/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" lang="en-us" />
    <t:style src="bootstrap.min.css" />
    <t:style src="font-awesome.min.css" />
    <t:script src="jquery-1.9.0.min.js" />
</head>
<body>
<header class="container">
    <h1>FOODit Test</h1>
</header>
<section class="container">
    <h2>Load Data</h2>
  <p>
      To start the test you need to click the load data button to setup the restaurant configuration data. <div><a class="btn btn-primary" href="/load/">Load Data</a></div>
  </p>

 <p>
     This will load json data for following four restaurants:
     <ul>
        <li><a href="/restaurant/bbqgrill/download">bbqgrill</a></li>
        <li><a href="/restaurant/butlersthaicafe/download">butlersthaicafe</a></li>
        <li><a href=/restaurant/jashanexquisiteindianfood/download>jashanexquisiteindianfood</a></li>
        <li><a href="/restaurant/newchinaexpress/download">newchinaexpress</a></li>
    </ul>
     The data includes the menu and existing orders that have been previously placed for the restaurant <code>RestaurantLoadData.java</code>
 </p>
    <h3>Menu/Orders</h3>
     <p>
         A menu contains meals which which have ids that link to the id of a line items on a order.
         </br>

         For example the following meal on the bbqgrill menu has a id 5:
     </p>
    <div class="highlight">
        <pre>{"id":5,"name":"Mixed Kebab","description":"","category":"Kebabs","sizeAndPrice":{"Regular":"7.50"},"mealTypeOptions":[{"name":"Side","multiSelect":true,"dropDown":false,"options":[{"label":"Chips","pricesForSize":{"Regular":"1.50"}},{"label":"Roast Potatoes","pricesForSize":{"Regular":"1.50"}},{"label":"Rice","pricesForSize":{"Regular":"1.50"}},{"label":"Taramasalata","pricesForSize":{"Regular":"1.50"}},{"label":"Houmous","pricesForSize":{"Regular":"1.50"}},{"label":"Tzatziki","pricesForSize":{"Regular":"1.50"}},{"label":"Greek Salad","pricesForSize":{"Regular":"3.00"}}]}],"startingFromPrice":"7.50"}</pre>
    </div>

    <p>And the order below has a line item of 5 hence we can deduce that this foodie has ordered a Mixed Kebab on this order</p>
    <div class="highlight">
        <pre>{"recVersion":5,<em>"orderId":5571834598653952</em>,"easyOrderNum":1,"created":"2014-05-17T20:09:27.697+01:00","storeId":"bbqgrill","storeName":"B-B-Q Grill","totalValue":7.50,"lineItems":[{<em>"id":5</em>,"total":"7.50","unitPrice":"7.50","quantity":1,"promotion":false,"mealOptions":[]}],"status":"Collected"....</pre>
    </div>

    <p>Refer to the readme file for full test details.</p>


</section>