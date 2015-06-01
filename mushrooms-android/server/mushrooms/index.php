<?php

libxml_disable_entity_loader(false);

if(!isset($_GET["version"])) {
	die("version is not defined");
}



$ver = intval($_GET["version"])+1;

$doc1 = new DOMDocument();
if(@!$doc1->load($ver.'.xml')) {
	echo "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<data version=\"".($ver-1)."\"></data>";
	die();
}



$doc2 = new DOMDocument();

for ($i = $ver+1; @$doc2->load($i.'.xml'); $i++) {

	// get 'res' element of document 1
	$res1 = $doc1->getElementsByTagName('data')->item(0); //edited res - items

	// iterate over 'item' elements of document 2
	$items2 = $doc2->getElementsByTagName('element');
	for ($j = 0; $j < $items2->length; $j ++) {
		$item2 = $items2->item($j);


		// import/copy item from document 2 to document 1
		$item1 = $doc1->importNode($item2, true);

		// append imported item to document 1 'res' element
		$res1->appendChild($item1);


	}
	$doc2 = new DOMDocument();
	$ver = $i;
}

$versAtr = $doc1->createAttribute('version');

$versAtr->value = $ver;

$doc1->getElementsByTagName('data')->item(0)->appendChild($versAtr);

echo $doc1->saveXML(); //edited -added saving into xml file




?>