INSERT INTO `RS_ITEM_EXTEN` (`item_iid`, `exten_key`, `exten_value`)
		VALUES('_default_1_','PUB','y1');


SELECT DISTINCT I.* dist FROM RS_ITEM AS I ,rs_item_exten AS IE 
	WHERE I.iid = IE.item_iid AND IE.exten_key='PUB' LIMIT 0,10


SELECT DISTINCT RS_ITEM.`seq_id` AS "RS_ITEM_seq_id", RS_ITEM.`iid` AS "RS_ITEM_iid", RS_ITEM.`name` AS "RS_ITEM_name", RS_ITEM.`url` AS "RS_ITEM_url", RS_ITEM.`access_type` AS "RS_ITEM_access_type", RS_ITEM.`add_ts` AS "RS_ITEM_add_ts", RS_ITEM.`modify_ts` AS "RS_ITEM_modify_ts", RS_ITEM.`status` AS "RS_ITEM_status", RS_ITEM.`text` AS "RS_ITEM_text", RS_ITEM.`owner_id` AS "RS_ITEM_owner_id"
   FROM RS_ITEM AS RS_ITEM , RS_ITEM_EXTEN AS ITEM_EXTEN 
   WHERE RS_ITEM.`iid` = ITEM_EXTEN.`item_iid` AND ITEM_EXTEN.`exten_key`='PUB' ORDER BY RS_ITEM.`seq_id` DESC LIMIT ?, ? 
 
SELECT COUNT(*) FROM (
  SELECT DISTINCT RS_ITEM.`seq_id`	
    FROM RS_ITEM AS RS_ITEM , RS_ITEM_EXTEN AS ITEM_EXTEN 
    WHERE RS_ITEM.`iid` = ITEM_EXTEN.`item_iid` AND ITEM_EXTEN.`exten_key`='PUB'
) AS m
   
UPDATE rs_item SET `status`=1  