var masterList = [];
var displayList = [];

function init() {
	getDisplayedData(masterList);
	displayList = masterList;
	buildDropdown("stopsList", "stopRef", "stopName", masterList);
	buildDropdown("linesList", "lineRef", "lineRefName", masterList);
}


function buildDropdown(id, taskRef, taskName, dataList) {
	console.log("building " + id + " dropdown");
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
	console.log("building table");
	let tbody = document.getElementById("busDataTableBody");
}

function clearFilter(id) {
	return;
}

function filterByLine() {
	console.log("filtering by line");
	let liList = document.getElementById("linesList").children;
	let checkboxList = [];
	let filteredList = [];
	//finding all the checked filters
	for(const li of liList) {
		let checkbox = li.querySelector("input[type='checkbox']");
		if (!checkbox) {
			console.error("filterBusData.filterByLine(): Cannot find checkbox");
			throw new Error("filterBusData.filterByLine(): Cannot find checkbox");
		}
		if(checkbox.checked)
		{
			checkboxList.push(checkbox.value);
			console.log(`checkbox value: ${checkbox.value}, checked: ${checkbox.checked}`);
		}
	}
	checkboxList.forEach(data => console.log(`checkbox.value: ${data}`));
	//filtering
	for(let checkbox of checkboxList) {
		filteredList = displayList.filter(data =>
			// console.log("dataLineRef: " + data["lineRef"] + " checkbox: " + checkbox)
			data["lineRef"] == checkbox
		);
	}
	console.log(`filtered list length ${filteredList.length}`);
	buildTable(filteredList);
}

function filterByStop() {
	console.log("Filtering by stop.");
}


function getDisplayedData() {
	console.log("building displayed data list");
	let dataList = []
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
	checkbox.onchange = ""
	label.htmlFor = value;
	label.appendChild(document.createTextNode(text));
	label.classList.add("filter-line-label");
	li.appendChild(checkbox);
	li.appendChild(label);
	container.appendChild(li);
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