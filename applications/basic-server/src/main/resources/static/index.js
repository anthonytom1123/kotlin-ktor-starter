function displayAllTasks() {
    clearTasksTable();
    fetchAllTasks().then(tasks => displayTasks(tasks))
}

function getAnalyzerUrl() {
    return "http://127.0.0.1:8887"
}

function displayTasksWithPriority() {
    clearTasksTable();
    const priority = readTaskPriority();
    fetchTasksWithPriority(priority).then(displayTasks)
}

function displayTasks(tasks) {
    console.log("displayTask Name")
    const tasksTableBody = tasksTable()
    tasks.forEach(task => {
        const newRow = taskRow(task);
        tasksTableBody.appendChild(newRow);
    });
}

function displayTask(name) {
    console.log("displayTask Name")
    fetchTaskWithName(name).then(t =>
        taskDisplay().innerHTML
            = `${t.priority} priority task ${t.name} with description "${t.description}"`
    )
}

function deleteTask(name) {
    deleteTaskWithName(name).then(() => {
        clearTaskDisplay();
        displayAllTasks();
    })
}

function deleteTaskWithName(name) {
    return sendDELETE(`/tasks/${name}`)
}

function addNewTask() {
    const task = buildTaskFromForm();
    sendPOST("/tasks", task).then(displayAllTasks);
}

function buildTaskFromForm() {
    return {
        name: getTaskFormValue("newTaskName"),
        description: getTaskFormValue("newTaskDescription"),
        priority: getTaskFormValue("newTaskPriority")
    }
}

function getTaskFormValue(controlName) {
    return document.addTaskForm[controlName].value;
}

function taskDisplay() {
    return document.getElementById("currentTaskDisplay");
}

function readTaskPriority() {
    return document.priorityForm.priority.value
}

function fetchTasksWithPriority(priority) {
    return sendGET("/byPriority/${priority}");
}

function fetchTaskWithName(name) {
    return sendGET("/byName/${name}");
}

function fetchAllTasks() {
    return sendGET("/tasks")
}

function sendGET(url) {
    return fetch(
        getAnalyzerUrl() + url,
        {headers: {'Accept': 'application/json'}}
    ).then(response => {
        if (response.ok) {
            console.log("GET "+ getAnalyzerUrl() + url +" response is ok")
            return response.json()
        }
        console.log("GET "+ getAnalyzerUrl() + url +"  response is not ok")
        return [];
    });
}

function sendPOST(url, data) {
    return fetch(getAnalyzerUrl + url, {
        method: 'POST',
        headers: {
            'Access-Control-Allow-Origin': '*',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
}

function sendDELETE(url) {
    return fetch(getAnalyzerUrl + url, {
        method: "DELETE"
    });
}

function tasksTable() {
    return document.getElementById("tasksTableBody");
}

function clearTasksTable() {
    tasksTable().innerHTML = "";
}

function clearTaskDisplay() {
    taskDisplay().innerText = "None";
}

function taskRow(task) {
    return tr([
        td(task.lineRef),
        td(task.lineName),
        td(task.stopName),
        td(task.directionRef),
        td(task.occupancy),
        td(task.arrivalTime),
        td(viewLink(task.name)),
        td(deleteLink(task.name)),
    ]);
}

function tr(children) {
    const node = document.createElement("tr");
    children.forEach(child => node.appendChild(child));
    return node;
}

function td(content) {
    const node = document.createElement("td");
    if (content instanceof Element) {
        node.appendChild(content)
    } else {
        node.appendChild(document.createTextNode(content));
    }
    return node;
}

function viewLink(taskName) {
    const node = document.createElement("a");
    node.setAttribute(
        "href", `javascript:displayTask("${taskName}")`
    )
    node.appendChild(document.createTextNode("view"));
    return node;
}

function deleteLink(taskName) {
    const node = document.createElement("a");
    node.setAttribute(
        "href", `javascript:deleteTask("${taskName}")`
    )
    node.appendChild(document.createTextNode("delete"));
    return node;
}