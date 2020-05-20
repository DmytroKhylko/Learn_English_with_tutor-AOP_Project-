var getUser = document.querySelector(".getUser")
var submitBtn = document.querySelector(".submitBtn")
var linkedUsers = document.querySelector(".linkedUsers")
submitBtn.addEventListener("click", async (event)=>{
  var foundUsers = await searchUsers(getUser.value)
  console.log(foundUsers)
  linkedUsers.innerHTML = ""
  var listOfLinkedUsers = foundUsers.map((userName)=>{
    var listElement = document.createElement("li")
    listElement.innerText = userName
    return listElement
  })
  listOfLinkedUsers.forEach((node)=>{
    linkedUsers.appendChild(node)
  })
})

async function searchUsers(searchString) {
  var result = await fetch(
    "/search",
    {
      method: "POST",
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({"searchString": searchString})
    }
  )
  return result.json()
}

