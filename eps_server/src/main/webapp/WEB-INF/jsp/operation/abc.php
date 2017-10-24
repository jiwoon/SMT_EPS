<?php
$shuzu = array();
	$a=array();// 将一个小时的上料数据放入对应的数组
	$num1=array(
		"suc"   => 55,
		"fail"  => 11,
		"total" => 66
		);
	array_push($a, $num1);

	$b=array();// 将一个小时的换料数据放入对应的数组
	$num2=array(
		"suc"   => 77,
		"fail"  => 3,
		"total" => 80
		);
	array_push($b, $num2);

	$c=array();// 将一个小时的抽检数据放入对应的数组
	$num3=array(
		"suc"   => 48,
		"fail"  => 7,
		"total" => 55
		);
	array_push($c, $num3);

	$d=array();// 将一个小时的全检数据放入对应的数组
	$num4=array(
		"suc"   => 40,
		"fail"  => 5,
		"total" => 45
		);
	array_push($d, $num4);


	$time = array();
	array_push($time,"14:00");


	$shuzu["feed"]=$a;
	$shuzu["changes"]=$b;
	$shuzu["somes"]=$c;
	$shuzu["alls"]=$d;
	$shuzu["time"] = $time;
	echo json_encode($shuzu);
 ?>