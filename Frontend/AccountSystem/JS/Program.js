"use strict";
$(function () {
    if (!isLogin()) {
        location.href = "login.html"
    } else {
        $(".header").load("header.html");
        $(".main").load("home.html");
        $(".footer").load("footer.html");
        $("#success-alert").hide();
    }
});
const baseURL = "http://localhost:8080/api/v1";
const header = {
    'Accept': 'application/json',
    'Content-Type': 'application/json',
    "Authorization": "Bearer " + localStorage.getItem('token')
}
var counter = 0;
function Account( username, firstname, lastname, role, departmentName, id) {
    this.username = username;
    this.firstName = firstname;
    this.lastName = lastname;
    this.role = role,
    this.departmentId = departmentName;
    this.id = id
}

function Department(id, departmentName, totalMember, type, createdDate) {
    this.id = ++counter;
    this.departmentName = departmentName;
    this.totalMember = totalMember;
    this.type = type;
    this.createdDate = createdDate
}

function Department(departmentName, totalMember, type, createdDate) {

    this.departmentName = departmentName;
    this.totalMember = totalMember;
    this.type = type;
    this.createdDate = createdDate
}

function ServiceContext(pageNumber, pageSize, sortField, sortType, search, role, departmentNameSearch) {
    this.page = pageNumber;
    this.size = pageSize;
    this.sortField = sortField;
    this.sortType = sortType;
    this.search = search;
    this.role = role;
    this.departmentName = departmentNameSearch;
}

function ServiceParam(property, value) {
    this.property = property;
    this.value = value;
}

var serviceParam = []
var serviceContextGlobal;
var departments = [];
var accounts = [];
var currentPage = 1;
var currentSize = 3;
var totalPage = 1;

function clickNavihome() {
    $(".main").load("home.html");
}

function clickNaviViewListAccount() {
    $(".main").load("viewListEmployee.html");
    initDepartment().then(
        function (value) {
            pushListDepartmentFormSearch();
            handleSubmitForm();
        }
    )
    serviceParam = [];
    initAccount();
}

function clickNaviViewListDepartment() {
    $(".main").load("viewListDepartment.html");
    initTableDepartment();
}


function isLogin() {
    if (localStorage.getItem("token")) {
        return true;
    }
    return false;
}

function logout() {
    localStorage.clear();
    location.href = "login.html"
}

function initAccount() {
    // serviceParam = [];
    let serviceContext = new ServiceContext(currentPage, currentSize, "username", "ASC", "", "");
    //Call API from server
    searchAccount(serviceContext);
}

function searchAccount(serviceContextInput) {
    console.log(serviceContextInput)
    // http://localhost:8080/api/v1/accounts/search/?page=0&size=4&search=a&sort=id,desc&role=ADMIN
    let url = baseURL + "/accounts/search/";
    $.ajax({
        url: url,
        type: 'POST',
        data: JSON.stringify(serviceContextInput),
        headers: header,
        error: function (err) {
            errorResponse(err);
        },
        success: function (data) {
            accounts = data.content;
            totalPage = data.totalPages;
            initTable();
            pagingTable(totalPage);
        }
    });
}
function pagingTable(pageAmount) {
    $('.pagination').empty();
    if (currentPage == 1) {
        $('.pagination').append('<li class=" page-item disabled"><a class="page-link" href="#" tabindex="-1" aria-disabled="true">Previous</a></li>');
    } else {
        $('.pagination').append('<li class="page-item " ><a class="page-link" href="#" tabindex="-1" onClick="changePage(' + (currentPage - 1) + ')">Previous</a></li>');
    }
    for (let index = 0; index < pageAmount; index++) {
        if (currentPage == (index + 1)) {
            $('.pagination').append('<li class="page-item " ><a class="page-link bg-secondary" href="#"  onClick="changePage(' + (index + 1) + ')">' + (index + 1) + '</a></li>');
        } else {
            $('.pagination').append('<li class="page-item " ><a class="page-link " href="#"  onClick="changePage(' + (index + 1) + ')">' + (index + 1) + '</a></li>');
        }
    }
    if (currentPage === totalPage) {
        $('.pagination').append('<li class=" page-item disabled"><a class="page-link" href="#" tabindex="-1" aria-disabled="Next">Next</a></li>');
    } else {
        $('.pagination').append('<li class="page-item "><a class="page-link" href="#" onClick="changePage(' + (currentPage + 1) + ')">Next</a></li>');
    }
}

function changePage(page) {
    if (page == currentPage) {
        return;
    }
    if (page > totalPage || page <= 0) {
        return;
    }
    currentPage = page;
    initAccount();
}

function initTable() {
    $('tbody').empty();
    accounts.forEach(function (item, index) {
        $('tbody').append(
            '<tr>' +
            '<td>' + (index + 1) + '</td>' +
            '<td>' + item.username + '</td>' +
            '<td>' + item.firstName + " " + item.lastName + '</td>' +
            '<td>' + item.role + '</td>' +
            '<td>' + item.department.name + '</td>' +
            '<td>' +
            '<a class="edit" title="Edit" data-toggle="tooltip" onclick="openUpdateModel(' + item.id + ')"><i class="material-icons">&#xE254;</i></a>' +
            '<a class="delete" title="Delete" data-toggle="tooltip"  onclick="confirmDelete(' + item.id + ')"><i class="material-icons">&#xE872;</i></a>' +
            '</td>' +
            '</tr>')
    });
}

function openAddModalAccount() {
    resetForm();
    ShowModalAccount(null);
}

function HideModal() {
    $('#myModal').attr('disabled','disabled');
}
function ShowModalAccount(id) {
    console.log(id)
    if(id){
        document.getElementById('username').disabled = true;
    } else{
        document.getElementById('username').disabled = false;
    }
    $('#departmentName').empty();
    initDepartment().then(
        function (value) {
            pushListDepartment("departmentName", id);
            $('#myModal').modal('show');
        }
    )
}

function pushListDepartment(id, choose) {
    $.each(departments, function (i, item) {
        $('#' + id).append($('<option>', {
            value: item.id,
            text: item.name,
            selected: item.id === choose ? true : false
        }));
    });
}

function pushListDepartmentFormSearch() {
    $.each(departments, function (i, item) {
        $('#departmentNameSearch').append($('<option>', {
            value: item.name,
            text: item.name
        }));
    });
}
function addAccount() {

    //lấy data từ Modal ra

    var username = $("#username").val();
    var firstname = $("#firstname").val();
    var lastname = $("#lastname").val();
    var role = $("#role").val();
    var departmentId = $("#departmentName").val();

    const account = new Account( username, firstname, lastname, role, departmentId);
    console.log(account);

    $.ajax({
        url: baseURL + '/accounts/create',
        type: 'POST',
        data: JSON.stringify(account), // body
        headers: header,
        success: function(data, textStatus, xhr) {
            HideModal();
            showSuccessAlert();
            currentPage = 1;
            initAccount();
        },
        error(err) {
            errorResponse(err);
        }
    });
}

function resetForm() {
    $("#accountId").val("");
    $("#firstname").val("");
    $("#lastname").val("");
    $("#username").val("");
    $("#fullname").val("");
    $("#role").val("");
    $("#departmentName").val("");
}

function openUpdateModel(id) {
    const account = accounts.find(x => x.id === id);
    // fill data for index
    document.getElementById("accountId").value = account.id;
    document.getElementById("username").value = account.username;
    document.getElementById("firstname").value = account.firstName;
    document.getElementById("lastname").value = account.lastName;
    document.getElementById("role").value = account.role;
    // document.getElementById("departmentName").value = account.department.name;

    ShowModalAccount(account.department.id);
}

function saveNewInfor() {

    //lấy data vừa nhập vào
    var id = document.getElementById("accountId").value;
    var username = document.getElementById("username").value;
    var firstname = document.getElementById("firstname").value;
    var lastname = document.getElementById("lastname").value;
    var role = document.getElementById("role").value;
    var departmentId = document.getElementById("departmentName").value;
    // Account( username, firstname, lastname, role, departmentName, id)
const accountUpdate = new Account(username, firstname, lastname, role, departmentId, id);
    $.ajax({
        url: baseURL + '/accounts/update',
        type: 'POST',
        data: JSON.stringify(accountUpdate), //parse sang file Json của body
        headers: header,
        success: function (result) {
            // success
            HideModal();
            showSuccessAlert();
            initAccount();
        },
        error(jqXHR, textStatus, errorThrown) {
            errorResponse(jqXHR);
        }
    });
}

function save() {
    var id = document.getElementById("accountId").value;
console.log(id)
    if (id == null || id == "") {
        addAccount();

    } else {
        saveNewInfor();
    }
}


function confirmDelete(id) {

    var index = accounts.findIndex(x => x.id == id);
    var fullname = accounts[index].fullname;

    var result = confirm("Do you want to delete account " + fullname + " ?");
    if (result) {
        deleteAccounts(id);
    }
}

function deleteAccounts(id) {

    $.ajax({
        url: baseURL + '/accounts/delete/' + id,
        headers: header,
        type: 'POST',
        success: function (result) {
            // success
            showSuccessAlert();
            initAccount();
        },
        error(jqXHR, textStatus, errorThrown) {
            errorResponse(jqXHR);
        }
    });

    // TODO validate
    // employees.splice(index, 1);
    // console.log(employees);
    // initTable();
}

function showSuccessAlert() {
    
    $("#success-alert").fadeTo(2000, 500).slideUp(500, function () {
        $("#success-alert").slideUp(500);
    });
}

async function initDepartment() {
    var url = baseURL + "/departments/get-all";
    //Call API from server
    return $.ajax({
        url: url,
        type: 'GET',
        headers: header,
        error: function (err) {
            errorResponse(err);
        },
        success: function (data) {
            departments = data;
            totalPage = data.totalPages;
        }
    });
}

function errorResponse(err) {
    console.log(err.responseJSON.message)
    $("#conten-error").html(err.responseJSON.message);
    $('#exampleModal').modal('show');

    // if(err.responseJSON.code === 401){
    //     console.log("abc")
    //     logout();
    // }
}
// ---------------------------------------------------------- Department ----------------------------------------------------------
function initTableDepartment() {
    initDepartment().then(
        function (value) {
            $('tbody').empty();
            value.forEach(function (item) {
                $('tbody').append(
                    '<tr>' +
                    '<td>' + item.id + '</td>' +
                    '<td>' + item.name + '</td>' +
                    '<td>' + item.totalMember + '</td>' +
                    '<td>' + item.type + '</td>' +
                    '<td>' + item.createdDate + '</td>' +
                    '<td>' +
                    '<a class="edit" title="Edit" data-toggle="tooltip" onclick="openUpdateModel(' + item.id + ')"><i class="material-icons">&#xE254;</i></a>' +
                    '<a class="delete" title="Delete" data-toggle="tooltip"  onclick="confirmDelete(' + item.id + ')"><i class="material-icons">&#xE872;</i></a>' +
                    '</td>' +
                    '</tr>')
            });
        }
    )
}

function openUpdateModel(id) {
    const account = accounts.find(x => x.id === id);
    // fill data for index
    document.getElementById("accountId").value = account.id;
    document.getElementById("username").value = account.username;
    document.getElementById("firstname").value = account.firstName;
    document.getElementById("lastname").value = account.lastName;
    document.getElementById("role").value = account.role;
    // document.getElementById("departmentName").value = account.department.name;

    ShowModalAccount(account.department.id);
}
function openAddModalDepartment() {
    resetFormDepartment();
    ShowModal();
}

function HideModal() {
    $('#myModal').modal('hide');
}

function ShowModal() {
    $('#myModalDepartment').modal('show');
}

function resetFormDepartment() {
    $("#id").val("");
    $("#departmentName").val("");
    $("#totalMember").val("");
    $("#type").val("");
    $("#createDate").val("");

}

function handleSubmitForm(){
    $("#test234").submit(function(e){
        e.preventDefault();

        let findAccount = $("#findAccount").val();
        let roleSearch = $("#roleSearch").val();
        let departmentNameSearch = $("#departmentNameSearch").val();
        currentPage = 1;
        let serviceContext = new ServiceContext(currentPage, currentSize, "username", "ASC",findAccount, roleSearch, departmentNameSearch);
        //Call API from server
        searchAccount(serviceContext);
    });
}