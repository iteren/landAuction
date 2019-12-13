function switchAddPlotTab(elementId) {
	var link = document.getElementById(elementId);
  	var importedContent = link.import;
	document.getElementById("addPlotMenu").innerHTML = importedContent.documentElement.innerHTML;
}
