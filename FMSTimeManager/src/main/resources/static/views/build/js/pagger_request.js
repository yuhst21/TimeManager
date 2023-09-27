/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
function render(id, pageindex, totalpage, gap,start_date,end_date,request_type,status) {
    var container = document.getElementById(id);
    var content = "";
    if (pageindex > gap + 1)
        content += "<a th:href=\"@{'/my_request?page=2'}\">First</a>";

    for (var i = pageindex - gap; i < pageindex; i++)
        if (i > 0)
            content += "<a th:href='" + i + "'>" + i + "</a>";

    content += "<span>" + pageindex + "</span>";

    for (var i = pageindex + 1; i <= pageindex + gap; i++)
        if (i < totalpage)
            content += "<a th:href='" + i + "'>" + i + "</a>";

    if (pageindex < totalpage - gap)
        content += "<a th:href='" + totalpage + "'>Last</a>";
    container.innerHTML = content;
}


