var myApp = angular.module("PhoneBook",['ngMaterial']);
var GroupMap = {}
var selectedid;
myApp.controller('ContactController',function EventController($scope,$http,$compile,$window){
    $scope.AddNewContacts = false;
    $scope.ListGroups = false;
    $scope.DisplayAll=true;
    $scope.gender = ['Female','Male'];
    $scope.SingleContacts = false;
    $scope.ContactList_group=false;
    $scope.AddGroups = false;
    $scope.groupName="";
    $scope.conlength=1;
    $scope.SingleContacts = false;
    $http.get('http://localhost:4567/group').then(function (data){
        $scope.groups = [];
        angular.forEach(data.data, function (value, index) {
            $scope.groups.push({ id: value.group_id, label: value.group_name });
        });
        for(let group of $scope.groups){
                GroupMap[group.label]=group.id;
        }
    })
    $http({
            method: 'GET',
            url: 'http://localhost:4567/contact'
            }).then(function(response) {
            $scope.contactdata = response.data;
            }, function(response) {
            
            });
    $scope.Add_Contacts = function(){
        $scope.conlength=1;
        $scope.AddNewContacts = true;
        $scope.ListGroups = false;
        $scope.DisplayAll = false;
        $scope.AddGroups = false;
        $scope.ContactList_group = false;
    }
    $scope.List_Group=function(){
        $scope.conlength=1;
        $scope.ListGroups = true;
        $scope.AddNewContacts = false;
        $scope.DisplayAll = false;
        $scope.AddGroups=false;
        $scope.ContactList_group = false;
        $http.get('http://localhost:4567/group').then(function (data){
        $scope.groups = [];
        angular.forEach(data.data, function (value, index) {
            $scope.groups.push({ id: value.group_id, label: value.group_name });
        });
        $scope.groupList = $scope.groups;
        for(let group of $scope.groups){
            GroupMap[group.label]=group.id;
        }
        })
    }
    $scope.save_Contacts=function(Details){
        $scope.conlength=1;
        Details.mname = "";
        Details.lname = "";
        Details.extension="1";
        Details.countryCode="+1";
        var GroupFinalMap = {};
   
        for(let group of Details.groups){
           GroupFinalMap[GroupMap[group]] =parseInt(GroupMap[group]);
        }
        if(Details.gender == 'F'){
            Details.gender = '1'
        }
        else{
             Details.gender = '0'
        }
        Details.groups = GroupFinalMap;
        console.log(Details.groups);
        console.log(JSON.stringify(Details));
        $http({
        url: 'http://localhost:4567/contact',
        method: "POST",
        data:JSON.stringify(Details),
        })
        .then(function(response) {
            $http({
            method: 'GET',
            url: 'http://localhost:4567/contact'
            }).then(function(response) {  
            $scope.contactdata = response.data;
            }, function(response) {
              alert("Network Error,Want to try Again");
            });
        $scope.AddNewContacts = false;
        $scope.ListGroups = false;
        $scope.DisplayAll = true;
        }, 
        function(response) { 
             console.log("not insuccess");
        });
     
    }
    $scope.Cancel_Contacts=function(){
        $scope.conlength=1;
        $scope.AddNewContacts = false;
        $scope.ListGroups = false;
        $scope.DisplayAll = true;
    }
    $scope.Details_contacts=function(event){
          $scope.conlength=1;
          $scope.SingleContacts = true;
          var id = $(event.target).attr('id')
          $http.get('http://localhost:4567/contact/'+id).then(function (data){
          $scope.fname = data.data.fname;
          $scope.mname = data.data.mname;
          $scope.lname = data.data.lname;
          $scope.contactNumber = data.data.contactInfo.contact_number;
          $scope.extension = data.data.contactInfo.extension;
          $scope.countryCode = data.data.contactInfo.country_code;
          $scope.emails = data.data.emails[0];
          $scope.Birthday = data.data.dob;
          })
          $scope.DisplayAll=false;
     }
     $scope.Details_GroupContacts=function(event){
         
         $scope.ListGroups =false;
         $scope.ContactList_group=true;
         selectedid = $(event.target).attr('id');
         console.log("selectedid");
         console.log(selectedid);
         $http.get('http://localhost:4567/contact/group/'+selectedid).then(function (data){
              
                $scope.Contact_GroupList=data.data;
                $scope.conlength = data.data.length;

          })
         
     }
     $scope.Delete_Groupcontacts=function(event){
         var r = confirm("Are you sure to delete from the group????");
         var contactid = $(event.target).attr('id');
         var groupid=selectedid;
         console.log(contactid);
          if(r==true){
             $http({
             method: 'DELETE',
             url: 'http://localhost:4567/contact/'+contactid+"/"+selectedid,
             })
             .then(function(response) {
                  $scope.List_Group();
            }, function(rejection) {
                 alert("Try Again,Not deleted");
             }); 
         }     
     }
     $scope.Delete_Group=function(event){
         var r = confirm("Are you sure to delete????");
         var id = $(event.target).attr('id');
         if(r==true){
             $http({
             method: 'DELETE',
             url: 'http://localhost:4567/group/'+id,
             })
             .then(function(response) {
                  $scope.List_Group();
            }, function(rejection) {
                 alert("Try Again,Not deleted");
             }); 
         } 
     }
    $scope.Delete_contacts=function(event){
         var r = confirm("Are you sure to delete????");
         var id = $(event.target).attr('id');
         if(r==true){
             $http({
             method: 'DELETE',
             url: 'http://localhost:4567/contact/'+id,
             })
             .then(function(response) {
                $window.location.reload();
            }, function(rejection) {
                 alert("Try Again,Not deleted");
             });
         }   
     }
    $scope.add_Groups=function(){
          $scope.AddGroups = true;
          $scope.ListGroups=false;
          $scope.DisplayAll = false;
          $scope.AddNewContacts=false;
      }
    $scope.Cancel_Groups=function(){
          $scope.AddGroups = false;
          $scope.ListGroups=true;
          $scope.DisplayAll = false;
          $scope.AddNewContacts=false;
      }
    $scope.save_Groups=function(user){
            $http({
            url: 'http://localhost:4567/group',
            method: "POST",
            data:JSON.stringify(user),
            })
           .then(function(response) {
            $scope.List_Group();
           console.log("bbb insuccess");

        }, 
        function(response) { 
             console.log("not insuccess");
        });
          $scope.AddGroups = false;
           $scope.ListGroups=true;
    }
    $scope.closeGroup=function(){
        $scope.ListGroups=false;
         $scope.DisplayAll = true;
    }  
    $scope.Cancel_form=function(){
        $scope.SingleContacts = false;
        $scope.DisplayAll = true;
    }
});
