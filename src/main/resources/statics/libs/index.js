$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + '/list',
        datatype: "json",
        colModel: [			
			{ label: 'ID', name: 'id', width: 30, key: true, hidden:true },
			{ label: '歌曲名', name: 'fileName', width: 200 , sortable: false, formatter: function(value, options, row){
			    var content = "<a href=\"javascript:;\" onclick=\"vm.download('" + row.id + "')\">" + row.fileName + "</a>";
			    return content;
			}},
			{ label: '专辑', name: 'albumName', width: 120 , sortable: false, formatter: function(value, options, row){
                return !isBlank(value) ? "《" + value + "》" : "";
            }},
			{ label: '时长', name: 'duration', width: 50 , sortable: false, formatter: function(value, options, row){
                var min = Math.floor((value/60) << 0);
                if (min < 10){
                    min = "0" + min;
                }
                var sec = Math.floor(value % 60);
                if (sec < 10){
                    sec = "0" + sec;
                }
                return min + ':' + sec;
            }},
            { label: '标准音质', name: 'standardSound', hidden:true },
            { label: '高品音质', name: 'highSound', hidden:true },
            { label: '无损FLAC', name: 'flacSound', hidden:true },
            { label: '无损APE', name: 'apeSound', hidden:true }
        ],
		viewrecords: true,
        height: 485,
        rowNum: 10,
		rowList : [10,20,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: false,
        subGrid : true,
        subGridOptions : {
            plusicon : "ace-icon fa fa-plus center bigger-110 blue",
            minusicon : "ace-icon fa fa-minus center bigger-110 blue",
            openicon : "ace-icon fa fa-chevron-right center orange"
        },
        subGridRowExpanded: function (subgridId, rowId) {
            var rowData = $("#jqGrid").jqGrid("getRowData", rowId);
            $("#" + subgridId).append("<table><tr><td>备注：</td><td>" + rowData.remark + "</td></tr></table>");
        },
        pager: "#jqGridPager",
        jsonReader : {
            root: "list",
            page: "currPage",
            total: "totalPage",
            records: "totalCount"
        },
        prmNames : {
            page:"page",
            rows:"limit",
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			key: null
		},
	},
	methods: {
        download: function (id) {
            var row = $("#jqGrid").jqGrid("getRowData", id);
            console.log("---------------下载准备---------------")
            window.location.href = "download?hash=" + row.standardSound;
            console.log("---------------下载完成---------------")
		},
        query: function () {
            vm.reload();
        },
		reload: function (event) {
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData:{'key': vm.q.key},
                page:page
            }).trigger("reloadGrid");
		}
	}
});