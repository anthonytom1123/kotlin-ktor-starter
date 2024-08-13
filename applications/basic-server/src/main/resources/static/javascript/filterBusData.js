var completeDataList = [];
var displayedDataList = [];

function init() {
	getDisplayedData(completeDataList);
	displayedDataList = completeDataList;
	buildDropdown("stopsList", "stopRef", "stopName");
	buildDropdown("linesList", "lineRef", "lineRefName");
}


function buildDropdown(id, taskRef, taskName) {
	console.log("building " + id + " dropdown");
	uniqueSet = new Set();
	completeDataList.forEach(function(task) {
		let filterItem = task[taskRef].toString() + "?!" + task[taskName];
		uniqueSet.add(filterItem);
	});
	Array.from(uniqueSet).forEach(function(filterString) {
		let trimmedFilterString = filterString.split("?!");
		populateFilterDropdown(id, parseInt(trimmedFilterString[0]), trimmedFilterString[1]);
	});
}

function clearFilter(id) {

}

function filterByLine() {

}

function filterByStop() {

}


function getDisplayedData(list) {
	console.log("building data list");
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
		list.push(task);
	}
}

function populateFilterDropdown(containerId, value, text) {
	console.log("building " + containerId +" value: " + value + " text: " + text);
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
	if(container.style.display == "none") {
		container.style.display = "block";
	}
	else {
		container.style.display = "none";
	}
}