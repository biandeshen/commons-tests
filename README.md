<div id="category"></div>
<link rel="stylesheet" href="http://yandex.st/highlightjs/6.2/styles/googlecode.min.css">

<script src="http://code.jquery.com/jquery-1.7.2.min.js"></script>
<script src="http://yandex.st/highlightjs/6.2/highlight.min.js"></script>

<script>hljs.initHighlightingOnLoad();</script>
<script type="text/javascript">
$(document).ready(function(){
  $("h2,h3,h4,h5,h6").each(function(i,item){
    var tag = $(item).get(0).localName;
    $(item).attr("id","wow"+i);
    $("#category").append('<a class="new'+tag+'" href="#wow'+i+'">'+$(this).text()+'</a></br>');
    $(".newh2").css("margin-left",0);
    $(".newh3").css("margin-left",20);
    $(".newh4").css("margin-left",40);
    $(".newh5").css("margin-left",60);
    $(".newh6").css("margin-left",80);
  });
});
</script>
<h1>Java相关的练习、学习、思考的一些简单代码
<h2>Java related exercises, learning, thinking about some simple code

---
目前主要学习的内容包括 《Java核心技术卷1》《Java并发编程实战》《Java7 并发编程实战手册》  
《Java 多线程与Socket实战微服务框架》等书,以及网络上看到的感兴趣的博客的学习与思考。
---