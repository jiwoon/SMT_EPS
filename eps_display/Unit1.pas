unit Unit1;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms, Dialogs, StdCtrls, ExtCtrls, DB, ADODB, Grids, DBGrids, DBCtrls, Mask, DateUtils, ShellAPI, DBTables, StrUtils, Math;

type
  TForm1 = class(TForm)
    selectQry: TADOQuery;
    mainQry: TADOQuery;
    operatorQry: TADOQuery;
    ds: TDataSource;
    refreshTimer: TTimer;
    DBConnection: TADOConnection;
    Panel1: TPanel;
    operatorNameLb: TLabel;
    operatorLb: TLabel;
    lineCb: TComboBox;
    workOrderLb: TLabel;
    workOrderCb: TComboBox;
    board_tybeLb: TLabel;
    boardTypeCb: TComboBox;
    materialNoLb: TLabel;
    scanMaterialNoLb: TLabel;
    scanMaterialNoNameLb: TLabel;
    materialNoNameLb: TLabel;
    lineseatLb: TLabel;
    scanLineseatLb: TLabel;
    scanLineSeatNameLb: TLabel;
    lineSeatNameLb: TLabel;
    Panel2: TPanel;
    dataGrid: TDBGrid;
    typeLb: TLabel;
    resultLb: TLabel;
    lineLb: TLabel;
    alertLb: TLabel;
    updateQry: TADOQuery;
    procedure dataGridDrawColumnCell(Sender: TObject; const Rect: TRect; DataCol: Integer; Column: TColumn; State: TGridDrawState);
    procedure lineCbChange(Sender: TObject);
    procedure workOrderCbChange(Sender: TObject);
    procedure boardTypeCbChange(Sender: TObject);
    procedure refreshTimerTimer(Sender: TObject);
    procedure updateTop();
    procedure lineCbDropDown(Sender: TObject);
    procedure workOrderCbDropDown(Sender: TObject);
    procedure FormCreate(Sender: TObject);
  private

    { Private declarations }
    procedure OnMouseWheel(var Msg: TMsg; var Handled: Boolean);
  public
    { Public declarations }
  end;

var
  Form1: TForm1;
  executing: Boolean;
  num: Real;
  board_type: Real;
  k: integer;

implementation

{$R *.dfm}

//根据数据集显示●或×
procedure TForm1.dataGridDrawColumnCell(Sender: TObject; const Rect: TRect; DataCol: Integer; Column: TColumn; State: TGridDrawState);
begin
  if (Column.FieldName = 'store_issue_result') then
  begin
    TDBGrid(Sender).Canvas.FillRect(Rect);

    if (TDBGrid(Sender).Fields[1].Value) then
    begin
      dataGrid.Canvas.Font.Color := clGreen;
      TDBGrid(Sender).Canvas.TextRect(Rect, Rect.Left + 2, Rect.Top + 2, '●');

    end
    else
    begin
      dataGrid.Canvas.Font.Color := clRed;
      TDBGrid(Sender).Canvas.TextRect(Rect, Rect.Left + 2, Rect.Top + 2, '×');
    end
  end
  else if (Column.FieldName = 'feed_result') then
  begin
    TDBGrid(Sender).Canvas.FillRect(Rect);
    if (TDBGrid(Sender).Fields[2].Value) then
    begin
      dataGrid.Canvas.Font.Color := clGreen;
      TDBGrid(Sender).Canvas.TextRect(Rect, Rect.Left + 2, Rect.Top + 2, ' ●');
    end
    else
    begin
      dataGrid.Canvas.Font.Color := clRed;
      TDBGrid(Sender).Canvas.TextRect(Rect, Rect.Left + 2, Rect.Top + 2, '×');

    end
  end
  else if (Column.FieldName = 'change_result') then
  begin
    TDBGrid(Sender).Canvas.FillRect(Rect);
    if (TDBGrid(Sender).Fields[3].Value) then
    begin
      dataGrid.Canvas.Font.Color := clGreen;
      TDBGrid(Sender).Canvas.TextRect(Rect, Rect.Left + 2, Rect.Top + 2, '●');
    end
    else
    begin
      dataGrid.Canvas.Font.Color := clRed;
      TDBGrid(Sender).Canvas.TextRect(Rect, Rect.Left + 2, Rect.Top + 2, '×');
    end
  end
  else if (Column.FieldName = 'check_result') then
  begin
    TDBGrid(Sender).Canvas.FillRect(Rect);
    if (TDBGrid(Sender).Fields[4].Value) then
    begin
      dataGrid.Canvas.Font.Color := clGreen;
      TDBGrid(Sender).Canvas.TextRect(Rect, Rect.Left + 2, Rect.Top + 2, '●');
    end
    else
    begin
      dataGrid.Canvas.Font.Color := clRed;
      TDBGrid(Sender).Canvas.TextRect(Rect, Rect.Left + 2, Rect.Top + 2, '×');
    end
  end
  else if (Column.FieldName = 'check_all_result') then
  begin
    TDBGrid(Sender).Canvas.FillRect(Rect);
    if (TDBGrid(Sender).Fields[5].Value) then
    begin
      dataGrid.Canvas.Font.Color := clGreen;
      TDBGrid(Sender).Canvas.TextRect(Rect, Rect.Left + 2, Rect.Top + 2, '●');
    end
    else
    begin
      dataGrid.Canvas.Font.Color := clRed;
      TDBGrid(Sender).Canvas.TextRect(Rect, Rect.Left + 2, Rect.Top + 2, '×');
    end
  end
  else if (Column.FieldName = 'first_check_all_result') then
  begin
    TDBGrid(Sender).Canvas.FillRect(Rect);

    if (TDBGrid(Sender).Fields[6].Value) then
    begin
      dataGrid.Canvas.Font.Color := clGreen;
      TDBGrid(Sender).Canvas.TextRect(Rect, Rect.Left + 2, Rect.Top + 2, '●');
    end
    else
    begin
      dataGrid.Canvas.Font.Color := clRed;
      TDBGrid(Sender).Canvas.TextRect(Rect, Rect.Left + 2, Rect.Top + 2, '×');
    end
  end
  else
  begin

  end;
end;

//刷新数据
procedure TForm1.updateTop();
var
  strsql: string;
  typename: string;
  temp: string;
  board_type: string;
begin
  executing := True;
  mainQry.SQL.Clear;
  operatorQry.SQL.Clear;
  //解析板面类型
  if (boardTypeCb.Text = '默认') then
  begin
    board_type := '0';
  end
  else if (boardTypeCb.Text = 'AB面') then
  begin
    board_type := '1';
  end
  else if (boardTypeCb.Text = 'A面') then
  begin
    board_type := '2';
  end
  else if (boardTypeCb.Text = 'B面') then
  begin
    board_type := '3';
  end;
  //查询program_item_visit表
  strsql := 'SELECT program_item_visit.* FROM program_item_visit INNER JOIN program ON program.`id`=program_item_visit.`program_id` WHERE line=''' + lineCb.Text + ''' AND work_order=''' + workOrderCb.Text + ''' AND board_type=''' + board_type + ''' ORDER BY last_operation_time DESC';
  mainQry.SQL.Add(strsql);
  mainQry.Open;
  mainQry.First;

  //填充顶部界面数据
  scanLineseatLb.Caption := mainQry.fieldbyname('scan_lineseat').AsString;
  lineseatLb.Caption := mainQry.fieldbyname('lineseat').AsString;
  materialNoLb.Caption := mainQry.fieldbyname('material_no').AsString;
  scanMaterialNoLb.Caption := mainQry.fieldbyname('scan_material_no').AsString;
  typename := mainQry.fieldbyname('last_operation_type').AsString;
  //填充右上角结果
  if (typename = '0') then
  begin
    typeLb.Caption := '上料';
    temp := temp + StrUtils.IfThen(mainQry.fieldbyname('feed_result').AsString = '0', 'FAIL', 'PASS');
  end
  else if (typename = '1') then
  begin
    typeLb.Caption := '换料';
    temp := temp + StrUtils.IfThen(mainQry.fieldbyname('change_result').AsString = '0', 'FAIL', 'PASS');
  end
  else if (typename = '2') then
  begin
    typeLb.Caption := '核料';
    temp := temp + StrUtils.IfThen(mainQry.fieldbyname('check_result').AsString = '0', 'FAIL', 'PASS');
  end
  else if (typename = '3') then
  begin
    typeLb.Caption := '全检';
    temp := temp + StrUtils.IfThen(mainQry.fieldbyname('check_all_result').AsString = '0', 'FAIL', 'PASS');
  end;
  if (typename = '4') then
  begin
    typeLb.Caption := '发料';
    temp := temp + StrUtils.IfThen(mainQry.fieldbyname('store_issue_result').AsString = '0', 'FAIL', 'PASS');
  end
  else if (typename = '5') then
  begin
    typeLb.Caption := '首检';
    temp := temp + StrUtils.IfThen(mainQry.fieldbyname('first_check_all_result').AsString = '0', 'FAIL', 'PASS');
  end;
  if StrUtils.AnsiContainsText(temp, 'PASS') then
  begin
    typeLb.Color := clGreen;
    resultLb.Color := clGreen;
  end
  else
  begin
    typeLb.Color := clRed;
    resultLb.Color := clRed;
  end;
  resultLb.Caption := temp;
  mainQry.Active := True;
  //填写操作员
  operatorQry.SQL.Add('SELECT operation.operator');
  operatorQry.SQL.Add('FROM program_item_visit INNER JOIN operation ON');
  operatorQry.SQL.Add('(operation.material_no=program_item_visit.material_no AND program_item_visit.`program_id`=operation.`program_id` AND program_item_visit.`lineseat`=operation.`lineseat`)');
  operatorQry.SQL.Add('WHERE operation.line=''' + lineCb.Text + ''' AND operation.work_order=''' + workOrderCb.Text + ''' AND operation.board_type=''' + board_type + '''');
  operatorQry.SQL.Add('ORDER BY operation.time DESC LIMIT 1');
  operatorQry.Open;
  operatorLb.Caption := operatorQry.fieldbyname('operator').AsString;
  executing := False;
end;

//选择线号后，查询出相应工单
procedure TForm1.lineCbChange(Sender: TObject);
var
  strsql: string;
begin
  selectQry.SQL.Clear;
  workOrderCb.items.Clear;
  boardTypeCb.items.Clear;
  strsql := 'select distinct work_order from program where line=''' + lineCb.Text + '''and work_order<>'''' and state=1';
  selectQry.sql.add(strsql);
  selectQry.Active := True;
  while not selectQry.eof do
  begin
    workOrderCb.items.add(selectQry.fieldByName('work_order').asstring);
    selectQry.next;
  end;

end;

//选择工单后，查询出相应板面类型
procedure TForm1.workOrderCbChange(Sender: TObject);
var
  strsql: string;
  board_type: string;
begin
  boardTypeCb.items.Clear;
  selectQry.SQL.Clear;
  strsql := ' select distinct board_type from program where work_order=''' + workOrderCb.Text + '''';
  selectQry.sql.add(strsql);
  selectQry.Active := True;
  while not selectQry.eof do
  begin
    board_type := selectQry.fieldByName('board_type').AsString;
    if (board_type = '0') then
    begin
      boardTypeCb.Items.Add('默认');
    end
    else if (board_type = '1') then
    begin
      boardTypeCb.Items.Add('AB面');
    end
    else if (board_type = '2') then
    begin
      boardTypeCb.Items.Add('A面');
    end
    else if (board_type = '3') then
    begin
      boardTypeCb.Items.Add('B面');
    end;
    selectQry.next;

  end;

end;



//选择板面类型后，更新数据，并显示表格
procedure TForm1.boardTypeCbChange(Sender: TObject);
begin
  updateTop;
  dataGrid.Show;
end;

//程序入口
procedure TForm1.FormCreate(Sender: TObject);
begin
  alertLb.Visible := False;
//  typeLb.Width := round(0.5 * Screen.Width);
//  typeLb.Height := round(0.55 * 0.5 * Screen.Height);
//  typeLb.Left := Round(0.5 * Screen.Width);
//  typeLb.Top := 0;
//
//  resultLb.Width := round(0.5 * Screen.Width);
//  resultLb.Height := round(0.55 * 0.5 * Screen.Height);
//  resultLb.Left := Round(0.5 * Screen.Width);
//  resultLb.Top := round(0.55 * 0.45 * Screen.Height);
//
//  lineLb.Width := round(0.1 * Screen.Width);
//  lineLb.Height := round(0.1 * 0.5 * Screen.Height);
//  lineLb.Left := Round(0.01 * Screen.Width);
//  lineLb.Top := Round(0.03 * Screen.Height);
//
//  board_tybeLb.Width := round(0.2 * Screen.Width);
//  board_tybeLb.Height := round(0.1 * 0.5 * Screen.Height);
//  board_tybeLb.Left := Round(0.01 * Screen.Width);
//  board_tybeLb.Top := Round(0.1 * Screen.Height);
//
//  lineSeatNameLb.Width := round(0.1 * Screen.Width);
//  lineSeatNameLb.Height := round(0.1 * 0.5 * Screen.Height);
//  lineSeatNameLb.Left := Round(0.01 * Screen.Width);
//  lineSeatNameLb.Top := round(0.45 * 0.5 * Screen.Height);
//
//  scanLineSeatNameLb.Width := round(0.1 * Screen.Width);
//  scanLineSeatNameLb.Height := round(0.1 * 0.5 * Screen.Height);
//  scanLineSeatNameLb.Left := Round(0.01 * Screen.Width);
//  scanLineSeatNameLb.Top := round(0.55 * 0.5 * Screen.Height);
//
//  materialNoNameLb.Width := round(0.1 * Screen.Width);
//  materialNoNameLb.Height := round(0.1 * 0.5 * Screen.Height);
//  materialNoNameLb.Left := Round(0.01 * Screen.Width);
//  materialNoNameLb.Top := round(0.65 * 0.5 * Screen.Height);
//
//  scanMaterialNoNameLb.Width := round(0.1 * Screen.Width);
//  scanMaterialNoNameLb.Height := round(0.1 * 0.5 * Screen.Height);
//  scanMaterialNoNameLb.Left := Round(0.01 * Screen.Width);
//  scanMaterialNoNameLb.Top := round(0.75 * 0.5 * Screen.Height);
//
//  lineseatLb.Width := round(0.36 * Screen.Width);
//  lineseatLb.Height := round(0.095 * 0.5 * Screen.Height);
//  lineseatLb.Left := Round(0.12 * Screen.Width);
//  lineseatLb.Top := round(0.45 * 0.52 * Screen.Height);
//
//  scanLineseatLb.Width := round(0.36 * Screen.Width);
//  scanLineseatLb.Height := round(0.095 * 0.5 * Screen.Height);
//  scanLineseatLb.Left := Round(0.12 * Screen.Width);
//  scanLineseatLb.Top := round(0.55 * 0.52 * Screen.Height);
//
//  materialNoLb.Width := round(0.36 * Screen.Width);
//  materialNoLb.Height := round(0.095 * 0.5 * Screen.Height);
//  materialNoLb.Left := Round(0.12 * Screen.Width);
//  materialNoLb.Top := round(0.65 * 0.52 * Screen.Height);
//
//  scanMaterialNoLb.Width := round(0.36 * Screen.Width);
//  scanMaterialNoLb.Height := round(0.095 * 0.5 * Screen.Height);
//  scanMaterialNoLb.Left := Round(0.12 * Screen.Width);
//  scanMaterialNoLb.Top := round(0.75 * 0.52 * Screen.Height);
//
//  boardTypeCb.Width := round(0.08 * Screen.Width);
//  boardTypeCb.Height := round(0.1 * 0.5 * Screen.Height);
//  boardTypeCb.Left := Round(0.1 * Screen.Width);
//  boardTypeCb.Top := round(0.1 * Screen.Height);
//
//  workOrderLb.Width := round(0.1 * Screen.Width);
//  workOrderLb.Height := round(0.1 * 0.5 * Screen.Height);
//  workOrderLb.Left := Round(0.19 * Screen.Width);
//  workOrderLb.Top := Round(0.03 * Screen.Height);
//
//  operatorLb.Width := round(0.2 * Screen.Width);
//  operatorLb.Height := round(0.1 * 0.5 * Screen.Height);
//  operatorLb.Left := Round(0.29 * Screen.Width);
//  operatorLb.Top := round(0.1 * Screen.Height);
//
//  operatorNameLb.Width := round(0.15 * Screen.Width);
//  operatorNameLb.Height := round(0.1 * 0.5 * Screen.Height);
//  operatorNameLb.Left := Round(0.17 * Screen.Width);
//  operatorNameLb.Top := round(0.1 * Screen.Height);
//
//  workOrderCb.Width := round(0.25 * Screen.Width);
//  workOrderCb.Height := round(0.1 * 0.5 * Screen.Height);
//  workOrderCb.Left := Round(0.24 * Screen.Width);
//  workOrderCb.Top := round(0.03 * Screen.Height);
//
//  dataGrid.Width := round(0.98 * Screen.Width);
//  dataGrid.Height := round(0.38 * Screen.Height);
//  dataGrid.Left := Round(0.1 * Screen.Width);
//  dataGrid.Top := round(0.4 * Screen.Height);

  executing := False;
  operatorQry.Active := False;
  dataGrid.Hide;
  Application.OnMessage := OnMouseWheel;
  Form1.WindowState := wsMaximized;
end;


//令表格可滚动
procedure TForm1.OnMouseWheel(var Msg: TMsg; var Handled: Boolean);
begin
  try
    if (Msg.message = WM_MouseWheel) and ((Screen.ActiveForm.ActiveControl.ClassName = 'TDBGrid') or (Screen.ActiveForm.ActiveControl.ClassName = 'TDBGridInplaceEdit')) then
    begin
      if Msg.wParam > 0 then
        SendMessage(Screen.ActiveForm.ActiveControl.Handle, WM_VSCROLL, SB_PAGEUP, 0)
      else
        SendMessage(Screen.ActiveForm.ActiveControl.Handle, WM_VSCROLL, SB_PAGEDOWN, 0);
      Handled := True;
    end;
  except
  end;
end;

//刷新数据定时器事件
procedure TForm1.refreshTimerTimer(Sender: TObject);
var
  min: TDateTime;
  strsql: string;
  board_type: string;
  lineseat: string;
  materialNo: string;
  change: TDateTime;
  check: TDateTime;
  problem: Boolean;
begin
  if (boardTypeCb.Text <> '') and (workOrderCb.Text <> '') and (executing = FALSE) then
  begin
    problem := False;
    //解析板面类型
    if (boardTypeCb.Text = '默认') then
    begin
      board_type := '0';
    end
    else if (boardTypeCb.Text = 'AB面') then
    begin
      board_type := '1';
    end
    else if (boardTypeCb.Text = 'A面') then
    begin
      board_type := '2';
    end
    else if (boardTypeCb.Text = 'B面') then
    begin
      board_type := '3';
    end;

    //刷新、核料周期
    updateTop;
    dataGrid.DataSource.DataSet.Active := FALSE;
    dataGrid.DataSource.DataSet.Active := TRUE;

    //全检周期
    if (k = 7200) then
    begin
      mainQry.First;
      min := mainQry.FieldByName('first_check_all_time').AsDateTime;
      while not mainQry.eof do
      begin

        if (mainQry.fieldByName('first_check_all_result').AsString <> 'PASS') then
        begin
          alertLb.Caption := '请开始首检！';
          problem := True;
          break;
        end;

        if (mainQry.fieldByName('first_check_all_time').AsDateTime < min) then
        begin
          min := mainQry.fieldByName('first_check_all_time').AsDateTime;
        end;

        min := DateUtils.IncHour(min, 2);

        if (DateUtils.CompareDateTime(Now, min) > 0) then
        begin
          updateQry.SQL.Clear;
          //更新program_item_visit表，令全检记录结果全为FAIL
          strsql := 'update program_item_visit set check_all_result = 0 where program_id in(SELECT id FROM program WHERE line=''' + lineCb.Text + ''' AND work_order=''' + workOrderCb.Text + ''' AND board_type=''' + board_type + '''';
          updateQry.SQL.Add(strsql);
          updateQry.Active := True;
          updateQry.ExecSQL;
          alertLb.Caption := '请开始全检';
          problem := True;
          Break;
        end;
        mainQry.Next;
      end;
      k := 0;
    end;

    mainQry.First;
    while not mainQry.eof do
    begin

      //全检计时器重置
      if (mainQry.fieldByName('feed_result').AsString <> 'PASS') then
      begin
        k := 0;
      end;

      //核料周期
      change := mainQry.fieldByName('change_time').AsDateTime;
      check := mainQry.fieldByName('check_time').AsDateTime;
      materialNo := mainQry.fieldByName('material_no').AsString;
      lineseat := mainQry.fieldbyName('lineseat').AsString;
      if (DateUtils.CompareDateTime(Now, DateUtils.IncMinute(change, 5)) > 0) and (DateUtils.CompareDateTime(check, change) < 0) then
      begin
        updateQry.SQL.Clear;
      //更新program_item_visit表，令指定核料记录结果为FAIL
        strsql := 'update program_item_visit set check_result = 0 where program_id in(SELECT id FROM program WHERE line=''' + lineCb.Text + ''' AND work_order=''' + workOrderCb.Text + ''' AND board_type=''' + board_type + ''') AND material_no = ''' + materialNo + ''' AND lineseat= ''' + lineseat + '''';
        updateQry.SQL.Add(strsql);
        updateQry.ExecSQL;
        alertLb.Caption := '请核料！';
        problem := True;
      end;
      mainQry.Next;
    end;

    if (problem = FALSE) then
    begin
      alertLb.Visible := False;
    end
    else
    begin
      alertLb.Visible := True;
      //触发报警
      //...
    end;

    k := k + 5;
    mainQry.First;
  end;
end;

procedure TForm1.lineCbDropDown(Sender: TObject);
begin
  workOrderCb.items.Clear;
  boardTypeCb.items.Clear;
end;

procedure TForm1.workOrderCbDropDown(Sender: TObject);
begin
  boardTypeCb.items.Clear;
end;

end.

