<%@ include file="/WEB-INF/jsp/topinclude.jspf" %>

<format:header>Welcome to the GeneDB website<br />Version 4.0</format:header>

<p>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
  <title>Drag and drop test page</title>
  <meta http-equiv="content-type" content="text/html; charset=utf-8" />
  <script src="<c:url value="/includes/scripts/"/>prototype.js</c:url>" type="text/javascript"></script>
  <script src="<c:url value="/includes/scripts/"/>scriptaculous.js</c:url>" type="text/javascript"></script>
  <script src="unittest.js" type="text/javascript"></script>
  <link rel="stylesheet" href="wtsi.css" type="text/css"/>
  <link rel="stylesheet" href="site.css" type="text/css"/>
  <script type="text/javascript">
    var ids = new Array();
   </script>
</head>
<body>
<h1>Test Drag-Drop</h1>
<h4>Drag the below images and drop them into the box named Query. Once you see the image into the Query box click outside the query box. To Delete 
images from the query box, drag them and drop them in the delete box.</h4>
<div class="content">
<div style="margin-bottom: 20px; height: 120px;">
<img
style="position: relative; z-index: 0; opacity: 0.99999; top: 0px; left: 0px;"
alt="c1" class="products" id="column_1" src="c1.png"/>
<script type="text/javascript">new Draggable('column_1', {revert:true})</script>
<img
style="position: relative; z-index: 0; opacity: 0.99999; top: 0px; left: 0px;"
alt="c2" class="products" id="column_2"
src="c2.png"/>
<script type="text/javascript">new Draggable('column_2', {revert:true})</script>
</div>
<div id="querybox" class="fieldset">
<div class="legend">Query Box</div>
<div id="items" class="content">
</div>
</div>
<br/><br />
<div id="waste" class="fieldset">
<div class="legend">Delete</div>
<div id="wastebin" class="content"></div>
</div>

<script type="text/javascript">Droppables.add('querybox', {accept: 'products',onDrop: function(element)
{
  var length = ids.length;
  var content1 = $('items').innerHTML;
  var id = element.id + Math.random();
  ids[length] = id;
  var alt = element.alt;
  var content2 = '  <img style="position: relative; z-index: 0; opacity: 0.99999; top: 0px; left: 0px;" alt="' + alt + '" class="query" id="' + id + '" src="' + alt + '.png"\/>';          
  $('items').innerHTML = content1 + content2;
  for(var x = 0; x <= ids.length; x++) {
	new Draggable(ids[x], {revert:true});
  }              
}
});</script>
<script type="text/javascript">Droppables.add('waste', {accept:'query', onDrop:function(element){Element.hide(element);}})</script>
</div>
</body>
</html>