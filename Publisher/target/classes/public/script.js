document.addEventListener("DOMContentLoaded", function () {
    // Generate and set a random publisher ID
    let publisherInput = document.getElementById("publisherId");
    let randomId = "publisher-" + Math.floor(Math.random() * 100000);
    publisherInput.value = randomId;

    // Handle topic selection
    document.getElementById("topic").addEventListener("change", function () {
        let newTopicGroup = document.getElementById("newTopicGroup");

        if (this.value === "Others") {
            newTopicGroup.style.display = "block"; // Show the custom topic input
        } else {
            newTopicGroup.style.display = "none";  // Hide the custom topic input
            document.getElementById("newTopic").value = ""; // Clear input
        }
    });
});

document.getElementById("publishButton").addEventListener("click", function () {
    let topic = document.getElementById("topic").value;
    let newTopic = document.getElementById("newTopic").value.trim();
    let message = document.getElementById("message").value.trim();
    let publisherId = document.getElementById("publisherId").value;

    // Use new topic if "Others" was selected
    let selectedTopic = (topic === "Others" && newTopic) ? newTopic : topic;

    if (selectedTopic === "Select Topic" || !message) {
        alert("Please select a topic and enter a message.");
        return;
    }

    const requestData = {
        id: Math.floor(Math.random() * 100000), // Integer ID
        topic: selectedTopic, // Selected or entered topic
        message: message,
        pid: publisherId, // Sends the random Publisher ID
        mid: Math.floor(Math.random() * 10000) // Random message ID
    };

  const API_BASE_URL = `${window.location.protocol}//${window.location.hostname}:8080`;

   console.log("API Base URL:", API_BASE_URL);

  fetch(`${API_BASE_URL}/publisher/publish`,
    {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(requestData)
    })
        .then(response => response.json())
        .then(data => {
            console.log("Message Sent:", data);
            addMessageToTable(requestData);
            document.getElementById("message").value = "";
            if (topic === "Others") {
                document.getElementById("newTopic").value = "";
            }
        })
        .catch(error => console.error("Error:", error));
});

function addMessageToTable(data) {
    const tableBody = document.querySelector("#messagesTable tbody");
    const row = tableBody.insertRow();
    row.insertCell(0).innerText = tableBody.rows.length;
    row.insertCell(1).innerText = data.topic;
    row.insertCell(2).innerText = data.message;
}
