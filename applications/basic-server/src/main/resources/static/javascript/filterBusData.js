let masterList = [];
let displayList = [];
let collectorUrl = "";
let webUrl = "";

function init(cUrl, wUrl) {
	collectorUrl = cUrl;
	webUrl = wUrl;
	console.log(`init: ${collectorUrl}, ${webUrl}`);
	masterList = getDisplayedData();
	displayList = masterList;
	buildDropdown("stopsList", "stopRef", "stopName", masterList);
	buildDropdown("linesList", "lineRef", "lineRefName", masterList);
}


function buildDropdown(id, taskRef, taskName, dataList) {
	console.log("filterBusData.buildDropdown: building " + id + " dropdown");
	uniqueSet = new Set();
	dataList.forEach(function(task) {
		let filterItem = task[taskRef] + "?!" + task[taskName];
		uniqueSet.add(filterItem);
	});
	Array.from(uniqueSet).sort().forEach(function(filterString) {
		let trimmedFilterString = filterString.split("?!");
		populateFilterDropdown(id, trimmedFilterString[0], trimmedFilterString[1]);
	});
}

function buildTable(dataList) {
	console.log("filterBusData.buildTable: building table");
	sortDataList(dataList);
	let table = document.getElementById("busDataTable");
	let tbody = table.getElementsByTagName('tbody')[0];
	clearTable(table);
	dataList.forEach(function(task) {
		let row = tbody.insertRow(-1);
		buildRow(row, task);
	});
}

function buildRow(row, task) {
	Object.keys(task).forEach(function(key) {
		switch (key) {
			case "lineRef":
				row.setAttribute("data-line-ref", task[key]);
				break;
			case "lineName":
				row.setAttribute("data-line-name", task[key]);
				break;
			case "stopRef":
				row.setAttribute("data-stop-ref", task[key]);
				break;
			default:
				let cell = row.insertCell(-1);
				cell.textContent = task[key];
		}
	});
}

function clearFilterDropdown(id) {
	console.log(`filterBusData.clearFilterDropdown: clearing dropdown ${id}`);
	let ul = document.getElementById(id);
	if (!ul && ul.tagName != "UL") {
		console.error(`filterBusData.clearFilterDropdown: element ID ${id} is not a <ul>!`);
		throw(`filterBusData.clearFilterDropdown: element ID ${id} is not a <ul>!`)
	}
	ul.innerHTML = "";
}

function clearTable(table) {
	console.log(`filterBusData.clearTable: clearing table ${table}`);
	while(table.rows.length > 1) {
		table.deleteRow(1); //delete the first row until empty
	}
}

function filterList(checkboxList, key) {
	let filteredList = [];
	if(checkboxList.length < 1) {
		filteredList = masterList;
	} else {
		for(let checkbox of checkboxList) {
			let tempList = masterList.filter(data =>
				data[key] == checkbox
			);
			filteredList.push(...tempList);
		}
	}
	return filteredList;
}

function filterByLine() {
	console.log("filterBusData.filterByLine: filtering by line");
	let liList = document.getElementById("linesList").children;
	let checkboxList = findAllCheckedFilters(liList);
	let filteredList = filterList(checkboxList, "lineRef");

	console.log(`filterBusData.filterByLine: filtered list length ${filteredList.length}`);
	buildTable(filteredList);
	displayList = filteredList;
	clearFilterDropdown("stopsList");
	buildDropdown("stopsList", "stopRef", "stopName", displayList);
}

function filterByStop() {
	console.log("filterBusData.filterByStop: filtering by stop");
	let liList = document.getElementById("stopsList").children;
	let checkboxList = findAllCheckedFilters(liList);
	let filteredList = filterList(checkboxList, "stopRef");

	console.log(`filterBusData.filterByStop: filtered list length ${filteredList.length}`);
	buildTable(filteredList);
}

function findAllCheckedFilters(liList) {
	let checkboxList = [];
	for(const li of liList) {
		let checkbox = li.querySelector("input[type='checkbox']");
		if (!checkbox) {
			console.error("filterBusData.findAllCheckedFilters(): Cannot find checkbox");
			throw new Error("filterBusData.findAllCheckedFilters(): Cannot find checkbox");
		}
		if(checkbox.checked)
		{
			checkboxList.push(checkbox.value);
		}
	}
	return checkboxList;
}

function getDisplayedData() {
	console.log("building displayed data list");
	let dataList = [];
	let table = document.getElementById("busDataTable");

	for (let i = 1; i < table.rows.length; i++) {
		let row = table.rows[i];
		let task = {
			"lineRefName": row.cells[0].textContent,
			"lineRef": row.getAttribute('data-line-ref'),
			"lineName": row.getAttribute('data-line-name'),
			"directionRef": row.cells[1].textContent,
			"stopRef": row.getAttribute('data-stop-ref'),
			"stopName": row.cells[2].textContent,
			"occupancy": row.cells[3].textContent,
			"arrivalTime": row.cells[4].textContent
		};
		dataList.push(task);
	}
	return dataList;
}

function populateFilterDropdown(containerId, value, text) {
	let container = document.getElementById(containerId);
	let li = document.createElement("li");
	let label = document.createElement("label");
	let checkbox = document.createElement("input");
	checkbox.type = "checkbox";
	checkbox.value = value;
	checkbox.name = text;
	checkbox.id = value;
	checkbox.onchange = "";
	label.htmlFor = value;
	label.appendChild(document.createTextNode(text));
	label.classList.add("filter-line-label");
	li.appendChild(checkbox);
	li.appendChild(label);
	container.appendChild(li);
}

function refresh() {
	console.log(`filterBusData.refresh: refreshing`);
	sendGet(collectorUrl);
	reloadPage();
}

function reloadPage() {
	window.location.reload(true);
}

function sendGet(url) {
	console.log(`filterBusData.sendGet: Connecting to ${url}`);
	fetch(url).then(response => {
		if(!response.ok) {
			throw new Error(`filterBusData.sendGet: Status ${response.status}`);
		}
		return response.text();
	}).then(data => {
		console.log(`filterBusData.sendGet: Data received - ${data}`);
	}).catch(error => {
		console.error(`filterBusData.sendGet: Error connected to ${url} - ${error}`);
	});
}

function sortDataList(dataList) {
	dataList.sort(function(taskA, taskB) {
		if(taskA.lineRef < taskB.lineRef) {
			return -1;
		} else if (taskA.lineRef > taskB.lineRef) {
			return 1;
		} else {
			if (taskA.stopRef < taskB.stopRef) {
				return -1;
			} else if (taskA.stopRef > taskB.stopRef) {
				return 1;
			} else {
				return 0;
			}
		}
	});
}

function toggleFilterDisplay(id) {
	let container = document.getElementById(id);
	let filters = document.getElementsByClassName("dropdown-content-container");
	if (filters.length <= 0) {
		console.log("Issue in toggleFilterDisplay, filter length: " + filters.length);
		return;
	}
	for(let i = 0; i < filters.length; i++) {
		if(filters[i].isEqualNode(container)){
			continue;
		}
		filters[i].style.display = "none";
	}
	if(container.style.display == "none" || !container.style.display) {
		container.style.display = "block";
	}
	else {
		container.style.display = "none";
	}
}